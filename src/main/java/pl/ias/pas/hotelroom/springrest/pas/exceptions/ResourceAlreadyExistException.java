package pl.ias.pas.hotelroom.springrest.pas.exceptions;

public class ResourceAlreadyExistException extends Exception {
    public ResourceAlreadyExistException() {
    }

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
