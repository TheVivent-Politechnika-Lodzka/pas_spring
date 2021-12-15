package pl.ias.pas.hotelroom.springrest.pas.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.IDontKnowException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAllocated;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;
import pl.ias.pas.hotelroom.springrest.pas.managers.ReservationManager;
import pl.ias.pas.hotelroom.springrest.pas.model.Reservation;

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
    public ResponseEntity createReservation(@RequestBody Reservation reservation) {
//        return Response.ok().build();

        UUID createdReservation;
        try {
            createdReservation = reservationManager.addReservation(reservation);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResourceAllocated resourceAllocated) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resourceAllocated.getMessage());
        }

        return ResponseEntity.created(URI.create("/reservation/" + createdReservation)).build();
    }

    //UPDATE\\
    @PostMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity updateReservation(@PathVariable("id") String id, @RequestBody Reservation reservation) {
        try {
            Reservation oldReservation = reservationManager.getReservationById(UUID.fromString(id));
            reservationManager.updateReservation(oldReservation, reservation);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    //DELETE\\
    @DeleteMapping(value = "/{id}")
    public ResponseEntity archiveReservation(@PathVariable("id") String id) {
        try {
            reservationManager.archiveReservation(UUID.fromString(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IDontKnowException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    //READ\\
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") String id) {
        Reservation reservation = reservationManager.getReservationById(UUID.fromString(id));

        if(reservation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(reservation);
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Reservation>> getAllReservation() {
        return ResponseEntity.ok(reservationManager.getAllReservations());
    }

    //BYID\\
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<List<Reservation>> getActiveReservationByClient(@PathVariable("clientId") String clientId, @RequestParam("archived") boolean archived) {
        UUID clienUUID = UUID.fromString(clientId);
        List<Reservation> reservations = reservationManager.searchReservations(clienUUID, archived);
        return ResponseEntity.ok(reservations);
    }
}
