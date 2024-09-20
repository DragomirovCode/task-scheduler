package ru.dragomirov.taskschedule.commons;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateException.class)
    public String handleDuplicateUserParameterException() {
        return "auth-error/error409";
    }
}