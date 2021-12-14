package pl.ias.pas.hotelroom.springrest.pas.model;

import lombok.*;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;

import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class HotelRoom {

    @EqualsAndHashCode.Include
    private UUID id;
    private int roomNumber;
    private int price;
    private int capacity;
    private String description;
    private boolean isAllocated = false;

    // nadawanie id to odpowiedzialność managera
    public HotelRoom(UUID id, int roomNumber, int price, int capacity, String description) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
    }

    public void validateRoomNumber() throws ValidationException {
        if (roomNumber < 0) {
            throw new ValidationException("Room number must be greater than 0");
        }
    }

    public void validatePrice() throws ValidationException {
        if (price < 0) {
            throw new ValidationException("Price must be greater than 0");
        }
    }

    public void validateCapacity() throws ValidationException {
        if (capacity < 0) {
            throw new ValidationException("Capacity must be greater than 0");
        }
    }

    public void validateDescription() throws ValidationException {
        if ("".equals(description)) {
            throw new ValidationException("Description cannot be empty");
        }
    }

    public void validate() throws ValidationException {
        validateRoomNumber();
        validatePrice();
        validateCapacity();
        validateDescription();
    }

}
