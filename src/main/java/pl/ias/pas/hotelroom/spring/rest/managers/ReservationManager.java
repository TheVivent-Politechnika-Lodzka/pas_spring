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

    public UUID addReservation(Reservation reservation) {

        User user = userDao.getUserById(reservation.getUserId());
        HotelRoom room = roomDao.getRoomById(reservation.getRoomId());

        //sprawdzenie czy nie jest juz przypadkiem wynajmowany
        if(room.isAllocated()) {
            throw new ResourceAllocatedException("Room is already occupied");
        }

        Instant endDate = reservation.getEndDate();
        Instant startDate = reservation.getStartDate();
        if (startDate == null) startDate = Instant.now();
        if (endDate !=null && startDate.isAfter(endDate)) { // sprawdzanie na startDate, bo endDate może być null
            throw new ValidationException("Start date is after end date");
        }

        Reservation newReservation = new Reservation(startDate, endDate, user.getId(), room.getId());
        room.setAllocated(true);

        return reservationDao.addReservation(newReservation);
    }

    public void endReseravation(UUID id) {
        reservationDao.endReservation(id);
    }

    public void updateReservation(UUID reservationToUpdate, Reservation update) throws ResourceNotFoundException {
        // sprawdzi czy user/room istnieje
        if (update.getUserId() != null) {
            userDao.getUserById(update.getUserId());
        }
        if(update.getRoomId() != null) {
            roomDao.getRoomById(update.getRoomId());
        }

        // TODO jakaś skomplikowana walidacja co do dat

        reservationDao.updateReservation(reservationToUpdate, update);
    }


    public List<Reservation> getEndedReservations() {
        return reservationDao.customSearch((reservation) -> !reservation.isActive());
    }

    public List<Reservation> getActiveReservations() {
        return reservationDao.customSearch((reservation) -> reservation.isActive());
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.getAllReservations();
    }

    public List<Reservation> searchReservations(UUID cliendId, boolean includeArchived) {
        // sprawdź czy user istnieje
        userDao.getUserById(cliendId);

        List<Reservation> result = new ArrayList<>();
        result = reservationDao.customSearch((reservation) ->
                reservation.getUserId().equals(cliendId)
                && (includeArchived || reservation.isActive())
        );

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("No reservations found");
        }

        return result;
    }

}
