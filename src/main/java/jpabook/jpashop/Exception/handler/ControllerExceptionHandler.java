package jpabook.jpashop.Exception.handler;

import jpabook.jpashop.entity.ErrorResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseEntity> ExceptionHandler(Exception e) throws Exception {
        if (e.getClass().equals(MethodArgumentNotValidException.class)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST.value())
                    .body(new ErrorResponseEntity((MethodArgumentNotValidException) e));
        }
        if (e.getClass().equals(BindException.class)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST.value())
                    .body(new ErrorResponseEntity((BindException) e));
        }
        throw e;
    }
}
