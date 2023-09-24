package com.example.restspringapp.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ExceptionBody {
    private String message;

    private Map<String, String> errors;

    public ExceptionBody(String message) {
        this.message = message;
    }


}
