package pl.ias.pas.hotelroom.spring.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource is allocated")
public class ResourceAllocatedException extends RuntimeException {
    public ResourceAllocatedException() {
    }

    public ResourceAllocatedException(String message) {
        super(message);
    }
}
