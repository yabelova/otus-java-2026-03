/*
 * Source: https://github.com/OtusTeam/Java-Pro (Otus Java Pro course materials, lesson L25-di)
 * Changes: Implemented the IoC container logic (processConfig, getAppComponent).
 */
package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.otus.appcontainer.exceptions.AppComponentsContainerException;
import ru.otus.appcontainer.exceptions.DuplicateComponentNameException;
import ru.otus.appcontainer.exceptions.NoUniqueComponentException;
import ru.otus.appcontainer.exceptions.NoSuchComponentException;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Object configInstance;
        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new AppComponentsContainerException("Cannot create config instance", e);
        }

        List<Method> componentMethods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .toList();

        for (Method method : componentMethods) {
            AppComponent annotation = method.getAnnotation(AppComponent.class);
            String name = annotation.name();

            if (appComponentsByName.containsKey(name)) {
                throw new DuplicateComponentNameException(name);
            }

            Object[] args = resolveParameters(method);

            method.setAccessible(true);
            Object bean;
            try {
                bean = method.invoke(configInstance, args);
            } catch (Exception e) {
                throw new AppComponentsContainerException("Cannot invoke method: " + method.getName(), e);
            }

            appComponents.add(bean);
            appComponentsByName.put(name, bean);
        }
    }

    private Object[] resolveParameters(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = getAppComponent(paramTypes[i]);
        }
        return args;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new AppComponentsContainerException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> matching = appComponents.stream()
                .filter(bean -> componentClass.isAssignableFrom(bean.getClass()))
                .toList();

        if (matching.isEmpty()) {
            throw new NoSuchComponentException("No component found for class: " + componentClass.getName());
        }
        if (matching.size() > 1) {
            throw new NoUniqueComponentException("Multiple components found for class: " + componentClass.getName());
        }

        return componentClass.cast(matching.getFirst());
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object bean = appComponentsByName.get(componentName);

        if (bean == null) {
            throw new NoSuchComponentException("No component found with name: " + componentName);
        }

        @SuppressWarnings("unchecked")
        C result = (C) bean;
        return result;
    }
}
