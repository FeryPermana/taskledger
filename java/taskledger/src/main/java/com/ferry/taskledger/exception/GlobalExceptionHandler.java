package com.ferry.taskledger.exception;

import com.ferry.taskledger.response.ValidationErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.ferry.taskledger.response.ApiResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ValidationErrorResponse handleValidationException(
                MethodArgumentNotValidException exception
        ) {

                Map<String, String> errors = new HashMap<>();

                exception.getBindingResult()
                        .getFieldErrors()
                        .forEach(error ->
                                errors.put(
                                        error.getField(),
                                        error.getDefaultMessage()
                                )
                        );

                return new ValidationErrorResponse(
                        400,
                        "Validation failed",
                        errors
                );
        }

        @ExceptionHandler(NoSuchElementException.class)
        public ApiResponse<Void> handleNotFoundException(
                NoSuchElementException exception
        ) {
                return new ApiResponse<>(
                        404,
                        exception.getMessage() != null
                                ? exception.getMessage()
                                : "Resource not found",
                        null
                );
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ApiResponse<Void> handleIllegalArgumentException(
                IllegalArgumentException exception
        ) {
                return new ApiResponse<>(
                        409,
                        exception.getMessage(),
                        null
                );
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ApiResponse<Void> handleHttpMessageNotReadableException(
                HttpMessageNotReadableException exception
        ) {
                return new ApiResponse<>(
                        400,
                        "Invalid request body",
                        null
                );
        }
}