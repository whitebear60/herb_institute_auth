package ua.edu.udhtu.whitebear60.herbinstituteauth.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.LoginException;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RegistrationException.class)
    protected ResponseEntity<Object> handleCreateAccountException(RegistrationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LoginException.class)
    protected ResponseEntity<Object> handleLoginException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
