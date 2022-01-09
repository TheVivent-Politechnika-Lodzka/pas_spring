package pl.ias.pas.hotelroom.spring.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.UUID;

//@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class HotelRoom {

    @EqualsAndHashCode.Include
    @Getter
    private UUID id;

    @Getter @Setter
    @Positive
    private int roomNumber;

    @Getter @Setter
    @Positive
    private int price;

    @Getter @Setter
    @Positive
    private int capacity;

    @Getter @Setter
    @NotBlank
    private String description;

    @Getter @Setter
    private boolean isAllocated = false;

    @Getter @Setter
    @JsonIgnore
    private boolean isActive = true;

    public HotelRoom(UUID id, int roomNumber, int price, int capacity, String description) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
    }

    //    public HotelRoom(int roomNumber, int price, int capacity, String description) {
//        this.id = UUID.randomUUID(); // wygenerowanie id, repo sprawdzi czy jest unikalne
//        this.roomNumber = roomNumber;
//        this.price = price;
//        this.capacity = capacity;
//        this.description = description;
//    }

    public HotelRoom(HotelRoom hotelRoom) {
        this.id = hotelRoom.getId();
        this.roomNumber = hotelRoom.getRoomNumber();
        this.price = hotelRoom.getPrice();
        this.capacity = hotelRoom.getCapacity();
        this.description = hotelRoom.getDescription();
        this.isAllocated = hotelRoom.isAllocated();
        this.isActive = hotelRoom.isActive();
    }

    //    public void validateRoomNumber() throws ValidationException {
//        if (roomNumber < 0) {
//            throw new ValidationException("Room number must be greater than 0");
//        }
//    }
//
//    public void validatePrice() throws ValidationException {
//        if (price < 0) {
//            throw new ValidationException("Price must be greater than 0");
//        }
//    }
//
//    public void validateCapacity() throws ValidationException {
//        if (capacity < 0) {
//            throw new ValidationException("Capacity must be greater than 0");
//        }
//    }
//
//    public void validateDescription() throws ValidationException {
//        if ("".equals(description)) {
//            throw new ValidationException("Description cannot be empty");
//        }
//    }
//
//    public void validate() throws ValidationException {
//        validateRoomNumber();
//        validatePrice();
//        validateCapacity();
//        validateDescription();
//    }

}
