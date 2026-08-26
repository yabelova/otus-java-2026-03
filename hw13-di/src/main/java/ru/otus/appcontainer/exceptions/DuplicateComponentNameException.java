package ru.otus.appcontainer.exceptions;

public class DuplicateComponentNameException extends AppComponentsContainerException {

    public DuplicateComponentNameException(String name) {
        super("Duplicate component name: " + name);
    }
}
