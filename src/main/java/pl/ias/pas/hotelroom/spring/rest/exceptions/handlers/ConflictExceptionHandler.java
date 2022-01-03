package pl.ias.pas.hotelroom.spring.rest.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class ConflictExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleEntityNotFound(IllegalArgumentException e){
        String conflictErrorString = "already exists";

        // jeżeli złapie błąd konwersji UUID to zwróć sensowną odpowiedź
        if (e.getMessage().contains(conflictErrorString)) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

        // jak to nie błąd konwersji to niech spring radzi sobie sam
        throw e;
    }
}
