package com.phildev.mls.donnees.patient.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({PatientException.class, ConstraintViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handle400Exception(HttpServletRequest request, Exception exception){
        Map<String, Object> response = new HashMap<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        int statusCode = HttpStatus.BAD_REQUEST.value();
        String method = request.getMethod();
        response.put("date", localDateTime);
        response.put("methode", method);
        response.put("statusCode", statusCode);

        if(request.getMethod().equals("GET")){
            response.put("params",  request.getParameterMap());
        }
        response.put("path", request.getRequestURL());
        switch (exception) {
            case ConstraintViolationException constraintViolationException -> {
                String messagesWithOutPropertyPath = (String) constraintViolationException.getConstraintViolations().stream()
                        .map(constraintViolation -> {
                            return constraintViolation == null ? "null" : constraintViolation.getMessage();
                        }).collect(Collectors.joining(", "));
                response.put("message", messagesWithOutPropertyPath);
            }
            case MethodArgumentNotValidException invalidArgumentException -> response.put("message", Objects.requireNonNull(invalidArgumentException.getDetailMessageArguments())[1]);
            case HttpMessageNotReadableException httpMessageNotReadableException -> {
                if(httpMessageNotReadableException.getMessage().contains("DateTimeParseException")){
                    response.put("message", "date format must be yyyy-MM-dd");
                }else{
                    response.put("message", httpMessageNotReadableException.getMostSpecificCause().toString());
                }
            }
            default -> {
                response.put("message", exception.getMessage());
            }
        }

        return response;
    }

    @ExceptionHandler({PatientNonTrouveException.class, StructureNonTrouveeException.class})
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
