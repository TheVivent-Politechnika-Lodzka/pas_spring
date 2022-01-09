package pl.ias.pas.hotelroom.spring.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ValidationException;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

//@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Reservation {

    @EqualsAndHashCode.Include
    @Getter
    private UUID id;

    @Getter @Setter
    private Instant startDate;

    @Getter @Setter
    private Instant endDate;

    @Getter @Setter
    private User user;

    @Getter @Setter
    private HotelRoom hotelRoom;

    public Reservation(UUID id, Instant startDate, Instant endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Reservation(Reservation reservation) {
        this.id = reservation.getId();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.user = reservation.getUser().copy();
        this.hotelRoom = new HotelRoom(reservation.getHotelRoom());
    }

    public boolean isActive() {
        if (endDate != null) {
            return endDate.isAfter(Instant.now());
        }
        return true;
    }

    //    public void setUserId(String userId) {
//        this.userId = UUID.fromString(userId);
//    }
//
//    public void setUserId(UUID userId) {
//        this.userId = userId;
//    }
//
//    public void setRoomId(String roomId) {
//        this.roomId = UUID.fromString(roomId);
//    }
//
//    public void setRoomId(UUID roomId) {
//        this.roomId = roomId;
//    }



//    public void setEndDate(Date endDate) {
//        if(this.startDate.after(endDate)) {
//            throw new RuntimeException();
//        }
//        this.endDate = endDate;
//    }

    public void validate() throws ValidationException {
        if(startDate.isAfter(endDate)) {
            throw new ValidationException("Start date must be before end date");
        }
    }

}
