package ru.itsjava.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){super(message);}
}
