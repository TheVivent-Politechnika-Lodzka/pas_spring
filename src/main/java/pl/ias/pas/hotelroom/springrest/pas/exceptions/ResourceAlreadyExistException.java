package pl.ias.pas.hotelroom.springrest.pas.exceptions;

public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException() {
    }

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
