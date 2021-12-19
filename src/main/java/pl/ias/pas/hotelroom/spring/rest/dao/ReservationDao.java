package pl.ias.pas.hotelroom.spring.rest.dao;

import com.pushtorefresh.javac_warning_annotation.Warning;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.spring.rest.model.Reservation;


import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;


@ApplicationScope
@Repository
public class ReservationDao {

//    private List<Reservation> reservationsRepository = Collections.synchronizedList(new ArrayList<>());
//    private List<Reservation> archiveRepository = Collections.synchronizedList(new ArrayList<>());
    private Map<UUID, Reservation> reservationsById = new HashMap<>();

    @Warning("This method is for testing only !!!")
    synchronized public void deleteReservation(UUID id) {
        if (reservationsById.containsKey(id)) {
            reservationsById.remove(id);
        } else {
            throw new ResourceNotFoundException("Reservation with id " + id + " does not exist");
        }
    }

    synchronized public UUID addReservation(Reservation reservation) {
        Reservation newReservation = new Reservation(reservation);

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

        if(update.getRoomId() != null) {
            reservation.setRoomId(update.getRoomId());
        }
        if(update.getUserId() != null) {
            reservation.setUserId(update.getUserId());
        }
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
