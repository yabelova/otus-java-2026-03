/*
 * Source: https://github.com/OtusTeam/Java-Pro (Otus Java Pro course materials, lesson L25-di)
 */
package ru.otus.appcontainer.api;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass);

    <C> C getAppComponent(String componentName);
}
