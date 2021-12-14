package pl.ias.pas.hotelroom.pasrest.managers;

import pl.ias.pas.hotelroom.pasrest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.pasrest.dao.ReservationDao;
import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.IDontKnowException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceAllocated;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceNotFoundException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ValidationException;
import pl.ias.pas.hotelroom.pasrest.model.HotelRoom;
import pl.ias.pas.hotelroom.pasrest.model.Reservation;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RequestScoped
public class ReservationManager {

    @Inject
    private UserDao userDao;
    @Inject
    private ReservationDao reservationDao;
    @Inject
    private HotelRoomDao roomDao;

    public Reservation getReservationById(UUID id) {
        return reservationDao.getReservationById(id);
    }

    public UUID addReservation(Reservation reservation) throws ResourceNotFoundException, ResourceAllocated, ValidationException {

        UUID id = UUID.randomUUID();
        Reservation newReservation;
        User user = userDao.getUserById(reservation.getUserId());
        HotelRoom room = roomDao.getRoomById(reservation.getRoomId());

        //sprawdzenie czy klient i pokoj istnieja
        if (user == null) {
            throw new ResourceNotFoundException("User does not exist");
        }
        if (room == null) {
            throw new ResourceNotFoundException("Room does not exist");
        }

        //sprawdzenie czy nie jest juz przypadkiem wynajmowany
        if(room.isAllocated()) {
            throw new ResourceAllocated("Room is already occupied");
        }

        reservation.validate();
        room.setAllocated(true);
        newReservation = new Reservation(id, user.getId(), room.getId());

        if (reservation.getStartDate() != null) {
            newReservation.setStartDate(reservation.getStartDate());
        } else {
            newReservation.setStartDate(System.currentTimeMillis());
        }

        if (reservation.getEndDate() != null) {
            newReservation.setEndDate(reservation.getEndDate());
        }

        return reservationDao.addReservation(newReservation);
    }

    public void archiveReservation(UUID id) throws ResourceNotFoundException, IDontKnowException {

        if (reservationDao.getReservationById(id) == null) {
            throw new ResourceNotFoundException("Reservation does not exist");
        }

        reservationDao.endReservation(id);
    }

    public void updateReservation(Reservation old, Reservation reservation) throws ResourceNotFoundException {
        if (reservationDao.getReservationById(old.getId()) == null) {
            throw new ResourceNotFoundException("Reservation does not exist");
        }

        if (reservation.getUserId() != null)
            if (userDao.getUserById(reservation.getUserId()) == null)
                throw new ResourceNotFoundException("User does not exist");

        if (reservation.getRoomId() != null)
            if (roomDao.getRoomById(reservation.getRoomId()) == null)
                throw new ResourceNotFoundException("Room does not exist");

        reservationDao.updateReservation(old, reservation);
    }


    public List<Reservation> getArchivedReservation() {
        return reservationDao.getArchivedReservations();
    }

    public List<Reservation> getActiveReservation() {
        return reservationDao.getActiveReservations();
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.getAllReservations();
    }

    public List<Reservation> searchReservations(UUID cliendId, boolean includeArchived) {
        List<Reservation> toReturn = new ArrayList<>();

        for (Reservation reservation : getActiveReservation()) {
            if (reservation.getUserId().equals(cliendId)) {
                toReturn.add(reservation);
            }
        }

        if (!includeArchived) return toReturn;
        for (Reservation reservation : getArchivedReservation()) {
            if (reservation.getUserId().equals(cliendId)) {
                toReturn.add(reservation);
            }
        }

        return toReturn;
    }

}
