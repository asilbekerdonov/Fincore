package com.example.spring.core.web.exception;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PROBLEM_BASE_URI = "https://api.example.com/problems/";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> Map.of(
                        "field", err.getField(),
                        "message", err.getDefaultMessage() != null ? err.getDefaultMessage() : "Invalid value"
                ))
                .toList();

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "validation-error",
                "Validation Failed",
                "One or more fields failed validation",
                request
        );
        problem.setProperty("code", "VALIDATION_ERROR");
        problem.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ProblemDetail> handleBadRequest(
            Exception ex,
            HttpServletRequest request
    ) {
        String detail = ex instanceof HttpMessageNotReadableException
                ? "Malformed JSON request body"
                : ex.getMessage();

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "bad-request",
                "Bad Request",
                detail,
                request
        );
        problem.setProperty("code", "BAD_REQUEST");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem = buildProblem(
                HttpStatus.NOT_FOUND,
                "resource-not-found",
                "Resource Not Found",
                ex.getMessage() != null ? ex.getMessage() : "Requested resource was not found",
                request
        );
        problem.setProperty("code", "NOT_FOUND");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(BusinessRuleConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflict(
            BusinessRuleConflictException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem = buildProblem(
                HttpStatus.CONFLICT,
                ex.getTypeSlug(), // например, "limit-exceeded" или "entity-disabled"
                ex.getTitle(),    // например, "Limit Exceeded"
                ex.getMessage(),
                request
        );
        problem.setProperty("code", ex.getErrorCode()); // например, "MAX_AMOUNT_EXCEEDED"
        if (ex.getMetadata() != null && !ex.getMetadata().isEmpty()) {
            problem.setProperty("metadata", ex.getMetadata());
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    private ProblemDetail buildProblem(
            HttpStatus status,
            String typeSlug,
            String title,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create(PROBLEM_BASE_URI + typeSlug));
        problem.setTitle(title);
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}