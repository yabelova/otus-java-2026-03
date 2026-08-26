package ru.otus.appcontainer.exceptions;

public class NoSuchComponentException extends AppComponentsContainerException {

    public NoSuchComponentException(String message) {
        super(message);
    }
}
