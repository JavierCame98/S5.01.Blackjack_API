package It_Academy.blackjack_api.infrastructure.web.exception;

import It_Academy.blackjack_api.application.exception.GameAlreadyFinishedException;
import It_Academy.blackjack_api.application.exception.GameNotFoundException;
import It_Academy.blackjack_api.application.exception.PlayerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 404 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(GameNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGameNotFound(
            GameNotFoundException ex) {

        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handlePlayerNotFound(
            PlayerNotFoundException ex) {

        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ── 422 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(GameAlreadyFinishedException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGameAlreadyFinished(
            GameAlreadyFinishedException ex) {

        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    // ── 400 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidation(
            WebExchangeBindException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ── 500 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneric(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private Mono<ResponseEntity<ErrorResponse>> build(HttpStatus status, String message) {
        return Mono.just(
                ResponseEntity.status(status)
                        .body(ErrorResponse.of(status, message))
        );
    }
}
