package pl.ias.pas.hotelroom.springrest.pas.dao;

import org.springframework.web.context.annotation.ApplicationScope;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.IDontKnowException;
import pl.ias.pas.hotelroom.springrest.pas.model.Reservation;


import java.util.*;
import java.sql.Date;


@ApplicationScope
public class ReservationDao {

    private List<Reservation> reservationsRepository = Collections.synchronizedList(new ArrayList<>());
//    private List<Reservation> archiveRepository = Collections.synchronizedList(new ArrayList<>());

    public UUID addReservation(Reservation reservation) {
        reservationsRepository.add(reservation);
        return reservation.getId();
    }

    public UUID addReservationToArchive(Reservation reservation) {

        getReservationById(reservation.getId()).setEndDate(System.currentTimeMillis());
        return reservation.getId();
    }

    public Reservation getReservationById(UUID id) {
        for (Reservation reservation : reservationsRepository) {
            if (reservation.getId().equals(id)) {
                return reservation;
            }
        }
        return null;
    }

    public void updateReservation(Reservation oldReservation, Reservation reservation) {
        if(reservation.getRoomId() != null) {
            oldReservation.setRoomId(reservation.getRoomId());
        }
        if(reservation.getUserId() != null) {
            oldReservation.setUserId(reservation.getUserId());
        }
        if(reservation.getStartDate() != null) {
            oldReservation.setStartDate(reservation.getStartDate());
        }
    }

    public void endReservation(UUID reservationId) throws IDontKnowException {
        Reservation res = getReservationById(reservationId);
        if (res.getEndDate() == 0 && new Date(System.currentTimeMillis()).after(res.getActualEndDate()))
            throw new IDontKnowException("Reservation with id " + reservationId + " already ended");
        res.setEndDate(System.currentTimeMillis());

    }

    public List<Reservation> getAllReservations() {
        return reservationsRepository;
    }

    public List<Reservation> getArchivedReservations() {
        List<Reservation> archivedReservations = new ArrayList<>();
        for (Reservation reservation : reservationsRepository) {
            if (!reservation.isActive()) {
                archivedReservations.add(reservation);
            }
        }
        return archivedReservations;
    }

    public List<Reservation> getActiveReservations() {
        List<Reservation> activeReservations = new ArrayList<>();
        for (Reservation reservation : reservationsRepository) {
            if (reservation.isActive()) {
                activeReservations.add(reservation);
            }
        }
        return activeReservations;
    }
}
