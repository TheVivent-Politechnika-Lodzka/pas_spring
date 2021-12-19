package pl.ias.pas.hotelroom.spring.rest.exceptions.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class UUIDConversionExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleEntityNotFound(IllegalArgumentException e){
        String uuidErrorString = "";
        try {
            UUID.fromString("");
        } catch (IllegalArgumentException ex) {
            uuidErrorString = ex.getMessage();
        }

        // jeżeli złapie błąd konwersji UUID to zwróć sensowną odpowiedź
        if (e.getMessage().contains(uuidErrorString)) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // jak to nie błąd konwersji to niech spring radzi sobie sam
        throw e;
    }
}
