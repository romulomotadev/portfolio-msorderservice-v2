package github.romulomotadev.msproducts.exception.exceptions.handler;

import github.romulomotadev.msproducts.dto.error.CustomErrorDto;
import github.romulomotadev.msproducts.dto.error.ValidateErrorDto;
import github.romulomotadev.msproducts.exception.exceptions.exceptions.DataDuplicateException;
import github.romulomotadev.msproducts.exception.exceptions.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    // TRATANDO ID NÃO ENCONTRADO
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDto> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomErrorDto err = new CustomErrorDto(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    // TRATANDO DADOS INVÁLIDOS
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorDto> argumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidateErrorDto err = new ValidateErrorDto(
                Instant.now(),
                status.value(),
                "Argument Not Valid",
                request.getRequestURI()
        );
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(err);
    }

    //TRATANDO DADOS DUPLICADOS
    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<CustomErrorDto> dataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        CustomErrorDto err = new CustomErrorDto(
                Instant.now(),
                status.value(),
                "Integrity constraint violation",
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    //TRATANDO DADOS DUPLICADOS
    @ExceptionHandler(DataDuplicateException.class)
    private ResponseEntity<CustomErrorDto> dataDuplicateException(DataDuplicateException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        CustomErrorDto err = new CustomErrorDto(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }
}