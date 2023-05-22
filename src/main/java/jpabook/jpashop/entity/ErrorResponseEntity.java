package jpabook.jpashop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseEntity {

    private String timestamp = new Date().toString();;
    private Integer status;

    private String method;
    private String error;
    private String message;
    private String path;

    public ErrorResponseEntity(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        this.message = fieldError.getField() + " : " + fieldError.getDefaultMessage();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.error = HttpStatus.BAD_REQUEST.getReasonPhrase();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            this.method = request.getMethod();
            this.path = request.getRequestURI();
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request가 존재하지 않습니다.");
        }
    }

    public ErrorResponseEntity(BindException e) {
        FieldError fieldError = e.getFieldError();
        this.message = fieldError.getField() + " : " + fieldError.getDefaultMessage();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.error = HttpStatus.BAD_REQUEST.getReasonPhrase();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            this.method = request.getMethod();
            this.path = request.getRequestURI();
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request가 존재하지 않습니다.");
        }
    }

    /*{
        "timestamp": "2023-05-22T06:32:48.276+00:00",
            "status": 400,
            "error": "Bad Request",
            "message": "Validation failed for object='orderSearch'. Error count: 1",
            "path": "/orders"
    }*/
}
