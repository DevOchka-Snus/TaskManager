package com.example.restspringapp.domain.exception;

public class ResourceMappingException extends RuntimeException{
    public ResourceMappingException(String message) {
        super(message);
    }
}
