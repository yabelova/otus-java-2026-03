package ru.otus.classfileapi;

import java.lang.classfile.*;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class LoggingTransformer implements ClassFileTransformer {

    private static final String TARGET_CLASS = "ru/otus/model/LoggableImpl";
    private static final String LOG_BY_AGENT = "ru.otus.annotation.LogByAgent";

    private static final ClassDesc LOGGING_HELPER = ClassDesc.of("ru.otus.logging.LoggingHelper");
    private static final String LOG_METHOD_NAME = "log";
    private static final MethodTypeDesc LOG_METHOD_TYPE = MethodTypeDesc.of(
            ConstantDescs.CD_void,
            ConstantDescs.CD_String,
            ConstantDescs.CD_String,
            ClassDesc.ofDescriptor("[Ljava/lang/Object;"));

    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> beingRedefined, ProtectionDomain protectionDomain,
                            byte[] classBytes) {

        if (className == null || !className.equals(TARGET_CLASS)) {
            return null;
        }

        try {
            return transformTargetClass(classBytes);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] transformTargetClass(byte[] originalBytes) {
        ClassFile classFile = ClassFile.of();
        ClassModel classModel = classFile.parse(originalBytes);

        return classFile.transformClass(classModel, (classBuilder, classElement) -> {
            if (classElement instanceof MethodModel methodModel && hasLogByAgentAnnotation(methodModel)) {

                String methodName = methodModel.methodName().stringValue();
                MethodTypeDesc methodType = methodModel.methodTypeSymbol();

                classBuilder.transformMethod(methodModel,
                        MethodTransform.transformingCode(createLogCodeTransform(methodName, methodType)));
            } else {
                classBuilder.with(classElement);
            }
        });
    }

    private static boolean hasLogByAgentAnnotation(MethodModel method) {
        return method.findAttribute(Attributes.runtimeVisibleAnnotations())
                .map(attr -> attr.annotations()
                        .stream()
                        .anyMatch(a -> a.classSymbol().equals(ClassDesc.of(LOG_BY_AGENT))))
                .orElse(false);
    }

    private static CodeTransform createLogCodeTransform(String methodName, MethodTypeDesc methodType) {
        boolean[] injected = {false};

        return (codeBuilder, codeElement) -> {
            if (!injected[0]) {
                injected[0] = true;
                injectLogging(codeBuilder, methodName, methodType);
            }
            codeBuilder.with(codeElement);
        };
    }

    private static void injectLogging(CodeBuilder code, String methodName, MethodTypeDesc methodType) {
        int paramCount = methodType.parameterCount();

        code.ldc("[agent]");
        code.ldc(methodName);

        code.bipush(paramCount)
                .anewarray(ConstantDescs.CD_Object);

        for (int i = 0; i < paramCount; i++) {
            ClassDesc paramType = methodType.parameterType(i);
            code.dup().bipush(i).loadLocal(TypeKind.from(paramType), code.parameterSlot(i));
            boxPrimitiveIfNeeded(code, paramType);
            code.aastore();
        }
        code.invokestatic(LOGGING_HELPER, LOG_METHOD_NAME, LOG_METHOD_TYPE);
    }

    private static void boxPrimitiveIfNeeded(CodeBuilder cb, ClassDesc type) {
        if (!type.isPrimitive())
            return;

        ClassDesc wrapper = switch (type.descriptorString()) {
            case "Z" -> ClassDesc.of("java.lang.Boolean");
            case "B" -> ClassDesc.of("java.lang.Byte");
            case "C" -> ClassDesc.of("java.lang.Character");
            case "S" -> ClassDesc.of("java.lang.Short");
            case "I" -> ClassDesc.of("java.lang.Integer");
            case "J" -> ClassDesc.of("java.lang.Long");
            case "F" -> ClassDesc.of("java.lang.Float");
            case "D" -> ClassDesc.of("java.lang.Double");
            default -> throw new IllegalArgumentException("Unknown primitive: " + type);
        };
        cb.invokestatic(wrapper, "valueOf", MethodTypeDesc.of(wrapper, type));
    }
}
