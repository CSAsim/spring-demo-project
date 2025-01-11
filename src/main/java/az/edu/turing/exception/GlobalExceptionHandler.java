package az.edu.turing.exception;

import az.edu.turing.model.constants.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.NOT_FOUND)
                        .errorMessage(e.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build()
                );
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<GlobalErrorResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.ALREADY_EXISTS)
                        .errorMessage(e.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build()
                );
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<GlobalErrorResponse> handleInvalidInputException(InvalidInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.INVALID_INPUT)
                        .errorMessage(e.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.BAD_REQUEST)
                        .errorMessage(e.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalErrorResponse> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.BAD_REQUEST)
                        .errorMessage(e.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .requestId(UUID.randomUUID())
                        .build()
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GlobalErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(GlobalErrorResponse.builder()
                                .errorCode(ErrorCode.BAD_REQUEST)
                                .errorMessage(e.getMessage())
                                .timeStamp(LocalDateTime.now())
                                .requestId(UUID.randomUUID())
                                .build()
                        );
    }
}
