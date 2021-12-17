package pl.ias.pas.hotelroom.springrest.pas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;

import javax.validation.constraints.NotNull;
import java.beans.BeanProperty;
import java.text.DateFormat;
import java.text.ParseException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

//@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Reservation {

    @EqualsAndHashCode.Include
    @Getter
    private UUID id;

    @Getter @Setter
    @NotNull
    private Instant startDate;

    @Getter @Setter
    private Instant endDate;

    @Getter @Setter
    @NotNull
    private UUID userId;

    @Getter @Setter
    @NotNull
    private UUID roomId;

    public Reservation(Instant startDate, Instant endDate, UUID userId, UUID roomId) {
        this.id = UUID.randomUUID();
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.roomId = roomId;
    }

    public Reservation(Instant startDate, UUID userId, UUID roomId) {
        this.startDate = startDate;
        this.userId = userId;
        this.roomId = roomId;
    }

    public Reservation(Reservation reservation) {
        this.id = reservation.getId();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.userId = reservation.getUserId();
        this.roomId = reservation.getRoomId();
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = Instant.parse(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = Instant.parse(endDate);
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
