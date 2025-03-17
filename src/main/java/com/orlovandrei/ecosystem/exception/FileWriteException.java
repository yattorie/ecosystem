package com.orlovandrei.ecosystem.exception;

public class FileWriteException extends RuntimeException {
    public FileWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}