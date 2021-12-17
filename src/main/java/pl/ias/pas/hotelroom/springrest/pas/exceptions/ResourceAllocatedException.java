package pl.ias.pas.hotelroom.springrest.pas.exceptions;

public class ResourceAllocatedException extends RuntimeException {
    public ResourceAllocatedException() {
    }

    public ResourceAllocatedException(String message) {
        super(message);
    }
}
