package ru.otus.proxy;

import ru.otus.annotation.LogByProxy;
import ru.otus.logging.LoggingHelper;
import ru.otus.model.Loggable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogInvocationHandler implements InvocationHandler {

    private final Loggable target;

    LogInvocationHandler(Loggable target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        boolean isLogged = targetMethod.isAnnotationPresent(LogByProxy.class);

        if (isLogged) {
            LoggingHelper.log("[proxy]", method.getName(), args);
        }

        return method.invoke(target, args);
    }
}
