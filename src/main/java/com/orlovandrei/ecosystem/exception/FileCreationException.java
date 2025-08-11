package com.orlovandrei.ecosystem.exception;

public class FileCreationException extends RuntimeException {
    public FileCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}