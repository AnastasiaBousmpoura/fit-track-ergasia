package gr.hua.dit.fittrack.web.rest.error;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.Instant;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.NoSuchElementException;
//
//@RestControllerAdvice(basePackages = "gr.hua.dit.fittrack.web.rest")
//public class GlobalRestExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiError> handleValidation(
//            MethodArgumentNotValidException ex,
//            HttpServletRequest req
//    ) {
//        Map<String, String> fieldErrors = new LinkedHashMap<>();
//        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
//            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
//        }
//
//        ApiError body = new ApiError(
//                Instant.now(),
//                400,
//                "Bad Request",
//                "Validation failed",
//                req.getRequestURI(),
//                fieldErrors
//        );
//        return ResponseEntity.badRequest().body(body);
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex, HttpServletRequest req) {
//        ApiError body = new ApiError(
//                Instant.now(), 404, "Not Found",
//                ex.getMessage(), req.getRequestURI(), null
//        );
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
//    }
//
//    @ExceptionHandler({BadCredentialsException.class})
//    public ResponseEntity<ApiError> handleUnauthorized(Exception ex, HttpServletRequest req) {
//        ApiError body = new ApiError(
//                Instant.now(), 401, "Unauthorized",
//                ex.getMessage(), req.getRequestURI(), null
//        );
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiError> handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
//        ApiError body = new ApiError(
//                Instant.now(), 403, "Forbidden",
//                ex.getMessage(), req.getRequestURI(), null
//        );
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handle500(Exception ex, HttpServletRequest req) {
//        ApiError body = new ApiError(
//                Instant.now(), 500, "Internal Server Error",
//                "Unexpected error", req.getRequestURI(), null
//        );
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
//    }
//}


import gr.hua.dit.fittrack.web.rest.error.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fields = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }

        ApiError body = new ApiError(
                Instant.now(),
                400,
                "validation_error",
                "Validation failed",
                req.getRequestURI(),
                fields
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(ResponseStatusException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getReason(),
                req.getRequestURI(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(),
                400,
                "business_error",
                ex.getMessage(),
                req.getRequestURI(),
                null
        );
        return ResponseEntity.badRequest().body(body);
    }
}
