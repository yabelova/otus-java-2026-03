package ru.otus.proxy;

import ru.otus.annotation.Log;
import ru.otus.helpers.LogHelper;
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
        boolean isLogged = targetMethod.isAnnotationPresent(Log.class);

        if (isLogged) {
            LogHelper.log("[proxy]", method.getName(), args);
        }

        return targetMethod.invoke(target, args);
    }
}
