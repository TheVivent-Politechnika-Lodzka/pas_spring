package pl.ias.pas.hotelroom.spring.rest.dao;

import com.pushtorefresh.javac_warning_annotation.Warning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.spring.rest.model.HotelRoom;
import pl.ias.pas.hotelroom.spring.rest.model.Reservation;
import pl.ias.pas.hotelroom.spring.rest.model.User;


import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;


@ApplicationScope
@Repository
public class ReservationDao {

    @Autowired
    private UserDao users;
    @Autowired
    private HotelRoomDao rooms;

    private Map<UUID, Reservation> reservationsById = new HashMap<>();

    @Warning("This method is for testing only !!!")
    synchronized public void deleteReservation(UUID id) {
        if (reservationsById.containsKey(id)) {
            reservationsById.remove(id);
        } else {
            throw new ResourceNotFoundException("Reservation with id " + id + " does not exist");
        }
    }

    synchronized public UUID addReservation(Reservation reservation, UUID userId, UUID roomId) {
        User user = users.getActualUser(userId);
        HotelRoom hotelRoom = rooms.getActualRoom(roomId);

        UUID id = UUID.randomUUID();
        Reservation newReservation = new Reservation(id, reservation.getStartDate(), reservation.getEndDate());
        newReservation.setUser(user);
        newReservation.setHotelRoom(hotelRoom);

        if (reservationsById.containsKey(newReservation.getId())) {
            throw new ResourceAlreadyExistException("Reservation with id " + newReservation.getId() + " already exists");
        }

        reservationsById.put(newReservation.getId(), newReservation);
        return newReservation.getId();
    }

    synchronized public Reservation getReservationById(UUID id) {
        Reservation reservation = reservationsById.get(id);
        if (reservation == null) {
            throw new ResourceNotFoundException("Reservation with id " + id + " does not exist");
        }
        return new Reservation(reservation);
    }

    synchronized public void updateReservation(UUID reservationToUpdate, Reservation update) {
        Reservation reservation = reservationsById.get(reservationToUpdate);

//        if(update.getRoomId() != null) {
//            reservation.setRoomId(update.getRoomId());
//        }
//        if(update.getUserId() != null) {
//            reservation.setUserId(update.getUserId());
//        }
        if(update.getStartDate() != null) {
            reservation.setStartDate(update.getStartDate());
        }
        if(update.getEndDate() != null) {
            reservation.setEndDate(update.getEndDate());
        }
    }

    synchronized public void endReservation(UUID reservationId) {
       Reservation reservation = reservationsById.get(reservationId);
        if (reservation == null) {
            throw new ResourceNotFoundException("Reservation with id " + reservationId + " does not exist");
        }
        reservation.setEndDate(Instant.now());
    }

    synchronized public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservationsById.forEach((id, reservation) -> reservations.add(new Reservation(reservation)));
        return reservations;
    }

    synchronized public List<Reservation> customSearch(Predicate<Reservation> lambda) {
        List<Reservation> reservations = new ArrayList<>();
        reservationsById.forEach((id, reservation) -> {
            if (lambda.test(reservation)) {
                reservations.add(new Reservation(reservation));
            }
        });
        return reservations;
    }

//    public List<Reservation> getArchivedReservations() {
//        List<Reservation> archivedReservations = new ArrayList<>();
//        for (Reservation reservation : reservationsRepository) {
//            if (!reservation.isActive()) {
//                archivedReservations.add(reservation);
//            }
//        }
//        return archivedReservations;
//    }
//
//    public List<Reservation> getActiveReservations() {
//        List<Reservation> activeReservations = new ArrayList<>();
//        for (Reservation reservation : reservationsRepository) {
//            if (reservation.isActive()) {
//                activeReservations.add(reservation);
//            }
//        }
//        return activeReservations;
//    }
}
