package com.smart.restaurantAppointment.Exception;

import com.smart.restaurantAppointment.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDTO>handleAppException(AppException ex, HttpServletRequest req) {
        log.warn("{} at {} : {} ",ex.getStatus(), req.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(new ErrorResponseDTO(ex.getStatus().value(),ex.getMessage(),LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex,HttpServletRequest req) {
        log.error("unexpected error at {}", req.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server error", LocalDateTime.now()));
    }

    private ResponseEntity<Map<String,Object>> buildResponse(HttpStatus status,String message) {
        Map<String, Object>body = Map.of(
                "status",status.value(),
                "error", message,
                "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation (MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ":" +err.getDefaultMessage())
                .collect(Collectors.joining(";"));
        log.warn("Validation failed at {} : {}", req.getRequestURI(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), message, LocalDateTime.now()));
    }
}
