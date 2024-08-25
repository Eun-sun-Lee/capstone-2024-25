package capstone.allbom_gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ExceptionResponse> handleTooManyRequestsException(final TooManyRequestsException e) {
        final ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage()
        );
        log.warn("[" + e.getClass() + "] " + errorResponse);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ExceptionResponse(429, errorResponse.toString()));
    }
}