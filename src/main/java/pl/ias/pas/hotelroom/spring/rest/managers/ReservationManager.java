package pl.ias.pas.hotelroom.spring.rest.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceAllocatedException;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ValidationException;
import pl.ias.pas.hotelroom.spring.rest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.spring.rest.dao.ReservationDao;
import pl.ias.pas.hotelroom.spring.rest.dao.UserDao;
import pl.ias.pas.hotelroom.spring.rest.model.HotelRoom;
import pl.ias.pas.hotelroom.spring.rest.model.Reservation;
import pl.ias.pas.hotelroom.spring.rest.model.User;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RequestScope
@Component
public class ReservationManager {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private HotelRoomDao roomDao;

    public Reservation getReservationById(UUID id) {
        return reservationDao.getReservationById(id);
    }

    public UUID addReservation(Reservation reservation, UUID userId, UUID roomId) {

        User user = userDao.getUserById(userId);
        HotelRoom room = roomDao.getRoomById(roomId);


        Instant endDate = reservation.getEndDate();
        Instant startDate = reservation.getStartDate();
        if (startDate == null) startDate = Instant.now();
        if (endDate !=null && startDate.isAfter(endDate)) {
            throw new ValidationException("Start date is after end date");
        }

        final Instant finalStartDate = startDate;
        final Instant finalEndDate = endDate;

        //sprawdzenie czy nie jest juz przypadkiem wynajmowany
        List<Reservation> tmpReservations = reservationDao.customSearch((res) -> {
            if (res.getStartDate().isAfter(finalStartDate) && res.getEndDate().isBefore(finalEndDate)) {
                return true;
            }
            return false;
        });

        if(!tmpReservations.isEmpty()) {
            throw new ResourceAllocatedException("Room is already occupied");
        }

        Reservation newReservation = new Reservation(UUID.randomUUID(), startDate, endDate);

        return reservationDao.addReservation(newReservation, user.getId(), room.getId());
    }

    public void endReseravation(UUID id) {
        reservationDao.endReservation(id);
    }

    // za dużo myślenia nad implementacją
//    public void updateReservation(UUID reservationToUpdate, Reservation update) {
//        // sprawdzi czy user/room istnieje
//        if (update.getUserId() != null) {
//            userDao.getUserById(update.getUserId());
//        }
//        if(update.getRoomId() != null) {
//            roomDao.getRoomById(update.getRoomId());
//        }
//
//
//        reservationDao.updateReservation(reservationToUpdate, update);
//    }


    public List<Reservation> getEndedReservations() {
        return reservationDao.customSearch((reservation) -> !reservation.isActive());
    }

    public List<Reservation> getActiveReservations() {
        return reservationDao.customSearch((reservation) -> reservation.isActive());
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.getAllReservations();
    }

    public List<Reservation> searchReservations(String cliendId, String roomId, boolean includeArchived) {
        // sprawdź czy user istnieje
//        userDao.getUserById(cliendId);

        List<Reservation> result;
        result = reservationDao.customSearch((reservation) ->
                reservation.getUser().getId().toString().contains(cliendId) &&
                reservation.getHotelRoom().getId().toString().contains(roomId) &&
                (includeArchived || reservation.isActive())
        );

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("No reservations found");
        }

        return result;
    }

}
