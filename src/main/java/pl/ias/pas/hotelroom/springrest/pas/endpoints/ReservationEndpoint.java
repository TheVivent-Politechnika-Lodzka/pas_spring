package pl.ias.pas.hotelroom.springrest.pas.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.IDontKnowException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAllocatedException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;
import pl.ias.pas.hotelroom.springrest.pas.managers.ReservationManager;
import pl.ias.pas.hotelroom.springrest.pas.model.Reservation;

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
    @PostMapping(consumes = "application/json")
    public ResponseEntity createReservation(@Valid @RequestBody Reservation reservation) {
        UUID createdReservation = reservationManager.addReservation(reservation);

        return ResponseEntity.created(URI.create("/reservation/" + createdReservation)).build();
    }

    //UPDATE\\
    @PostMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity updateReservation(@PathVariable("id") String reservationToUpdate, @RequestBody Reservation update) {
        UUID id = UUID.fromString(reservationToUpdate);
        reservationManager.updateReservation(id, update);

        return ResponseEntity.ok().build();
    }

    //DELETE\\ // TODO przemyśleć czy to jest dobrze
    @DeleteMapping(value = "/{id}")
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
    @GetMapping(value = "/search/{clientId}", produces = "application/json")
    public ResponseEntity<List<Reservation>> getActiveReservationByClient(
            @PathVariable("clientId") String clientId,
            @RequestParam(value = "archived", required = false, defaultValue = "false") boolean archived
    ) {
        UUID clienUUID = UUID.fromString(clientId);
        List<Reservation> reservations = reservationManager.searchReservations(clienUUID, archived);
        return ResponseEntity.ok(reservations);
    }
}
