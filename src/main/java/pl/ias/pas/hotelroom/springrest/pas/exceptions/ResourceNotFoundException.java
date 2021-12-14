package pl.ias.pas.hotelroom.springrest.pas.exceptions;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
