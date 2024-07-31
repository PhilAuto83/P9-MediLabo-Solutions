package com.phildev.mls.diabete.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHander {


    @ExceptionHandler({DonneesPatientNonTrouvees.class, NoteNonTrouveeException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle404Exception(HttpServletRequest request, RuntimeException exception){
        Map<String, Object> response = new HashMap<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        String error = exception.getMessage();
        int statusCode = HttpStatus.NOT_FOUND.value();
        String method = request.getMethod();
        String path = request.getRequestURI();
        response.put("date", localDateTime);
        response.put("method", method);
        response.put("path", path);
        response.put("statusCode", statusCode);
        response.put("message", error);
        return response;
    }
}
