package com.article.backend.exception;

public class NotAnImageFileException extends RuntimeException {

    public NotAnImageFileException(String message) {
        super(message);
    }

}
