package com.example.verdandi.controller;

import com.example.verdandi.exception.DatabaseOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DatabaseOperationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDatabaseOperation(DatabaseOperationException ex, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("message", "A database error occurred.");
        return "error/500";
    }
}
