package pl.ias.pas.hotelroom.springrest.pas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class IDontKnowException extends RuntimeException {
    public IDontKnowException() {
    }

    public IDontKnowException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " ¯\\_(ツ)_/¯";
    }
}
