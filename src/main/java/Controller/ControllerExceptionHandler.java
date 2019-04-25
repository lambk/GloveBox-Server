package Controller;

import Utility.Exceptions.UnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = UnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorised(UnauthorizedException e, WebRequest request) {
        final String message = e.getMessage() != null ? e.getMessage() : "You are not authorised to perform this action";
        return handleExceptionInternal(e, message,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
}
