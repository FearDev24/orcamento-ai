package com.orcamento.orcamento_ai.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarErroGeral(Exception e) {
        return ResponseEntity
                .internalServerError()
                .body("Erro interno: " + e.getMessage());
    }
}