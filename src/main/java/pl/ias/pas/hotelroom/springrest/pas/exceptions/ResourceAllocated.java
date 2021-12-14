package pl.ias.pas.hotelroom.springrest.pas.exceptions;

public class ResourceAllocated extends Exception {
    public ResourceAllocated() {
    }

    public ResourceAllocated(String message) {
        super(message);
    }
}
