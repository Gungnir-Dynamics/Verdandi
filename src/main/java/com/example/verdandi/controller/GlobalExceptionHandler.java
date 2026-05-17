package com.example.verdandi.controller;

import com.example.verdandi.exception.AccessDeniedException;
import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProfileNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        model.addAttribute("message", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(DatabaseOperationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDatabaseOperation(DatabaseOperationException ex, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("message", "A database error occurred.");
        return "error/500";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleDatabaseOperation(AccessDeniedException ex, Model model) {
        return "error/403";
    }
}
