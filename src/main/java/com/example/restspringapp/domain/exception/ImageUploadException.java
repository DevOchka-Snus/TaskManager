package com.example.restspringapp.domain.exception;

public class ImageUploadException extends RuntimeException{
    public ImageUploadException() {
        super();
    }

    public ImageUploadException(String message) {
        super(message);
    }
}
