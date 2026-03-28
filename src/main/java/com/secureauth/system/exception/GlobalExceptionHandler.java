package com.secureauth.system.exception;

import com.secureauth.system.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed",
                        request.getDescription(false).replace("uri=", ""), details));
    }

    @ExceptionHandler({
            ResourceAlreadyExistsException.class,
            AccountLockedException.class,
            InvalidTokenException.class,
            TooManyRequestsException.class,
            BadCredentialsException.class,
            UsernameNotFoundException.class,
            ConstraintViolationException.class,
            ResourceNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleKnownExceptions(RuntimeException ex, HttpServletRequest request) {
        HttpStatus status = mapStatus(ex);
        log.warn("Handled business exception: {}", ex.getMessage());
        return ResponseEntity.status(status)
                .body(buildErrorResponse(status, ex.getMessage(), request.getRequestURI(), List.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred",
                        request.getRequestURI(),
                        List.of()));
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message, String path, List<String> details) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .details(details)
                .build();
    }

    private HttpStatus mapStatus(RuntimeException ex) {
        if (ex instanceof ResourceAlreadyExistsException) {
            return HttpStatus.CONFLICT;
        }
        if (ex instanceof AccountLockedException) {
            return HttpStatus.LOCKED;
        }
        if (ex instanceof InvalidTokenException || ex instanceof BadCredentialsException || ex instanceof UsernameNotFoundException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (ex instanceof TooManyRequestsException) {
            return HttpStatus.TOO_MANY_REQUESTS;
        }
        if (ex instanceof ConstraintViolationException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (ex instanceof ResourceNotFoundException) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.BAD_REQUEST;
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}
