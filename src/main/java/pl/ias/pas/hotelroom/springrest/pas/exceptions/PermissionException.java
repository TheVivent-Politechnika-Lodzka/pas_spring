package pl.ias.pas.hotelroom.springrest.pas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PermissionException extends RuntimeException {

    public PermissionException(String message) {
        super(message);
    }
}
