package pl.ias.pas.hotelroom.spring.rest.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ias.pas.hotelroom.spring.rest.managers.ReservationManager;
import pl.ias.pas.hotelroom.spring.rest.model.Reservation;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

//@RequestScope
@RestController
@RequestMapping("/reservation")
public class ReservationEndpoint {

    @Autowired
    private ReservationManager reservationManager;

    // przykładowe zapytanie tworzące nowej rezerwacji, trza niestety uzupelniac
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/reservation uid=  rid=


    //CREATE\\
    @PostMapping(value="/{userId}/{roomId}", consumes = "application/json")
    public ResponseEntity createReservation(@Valid @RequestBody(required = false) Reservation reservation,
            @PathVariable("userId") String userId, @PathVariable("roomId") String roomId
    ) {
        if (reservation == null) {
            reservation = new Reservation();
        }
        UUID userUUID = UUID.fromString(userId);
        UUID roomUUID = UUID.fromString(roomId);
        UUID createdReservation = reservationManager.addReservation(reservation, userUUID, roomUUID);

        return ResponseEntity.created(URI.create("/reservation/" + createdReservation)).build();
    }

    //UPDATE\\
//    @PostMapping(value = "/{id}", consumes = "application/json")
//    public ResponseEntity updateReservation(@PathVariable("id") String reservationToUpdate, @RequestBody Reservation update) {
//        UUID id = UUID.fromString(reservationToUpdate);
//        reservationManager.updateReservation(id, update);
//
//        return ResponseEntity.ok().build();
//    }

    //DELETE\\ // TODO przemyśleć czy to jest dobrze
    @RequestMapping(value = "/end/{id}", method = RequestMethod.HEAD)
    public ResponseEntity archiveReservation(@PathVariable("id") String id) {
        reservationManager.endReseravation(UUID.fromString(id));

        return ResponseEntity.ok().build();
    }

    //READ\\
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") String id) {
        Reservation reservation = reservationManager.getReservationById(UUID.fromString(id));

        return ResponseEntity.ok(reservation);
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Reservation>> getAllReservation() {
        return ResponseEntity.ok(reservationManager.getAllReservations());
    }

    //SEARCH\\
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<List<Reservation>> getActiveReservationByClient(
            @RequestParam(value = "clientId", required = false, defaultValue = "") String clientId,
            @RequestParam(value = "roomId", required = false, defaultValue = "") String roomId,
            @RequestParam(value = "archived", required = false, defaultValue = "false") boolean archived
    ) {
        List<Reservation> reservations = reservationManager.searchReservations(clientId, roomId, archived);
        return ResponseEntity.ok(reservations);
    }
}
