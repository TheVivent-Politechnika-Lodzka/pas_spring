package pl.ias.pas.hotelroom.springrest.pas.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAllocated;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;
import pl.ias.pas.hotelroom.springrest.pas.managers.HotelRoomManager;
import pl.ias.pas.hotelroom.springrest.pas.model.HotelRoom;

import java.net.URI;
import java.util.List;
import java.util.UUID;

//@RequestScope
@RestController
@RequestMapping("/room")
public class HotelRoomEndpoint {

    @Autowired
    private HotelRoomManager roomManager;

    // przykładowe zapytanie tworzące nowego użytkownika
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/room roomNumber=2 price=100 capacity=300 description=cosy

    //CREATE\\
    @PostMapping(value="/", consumes = "application/json")
    public ResponseEntity createRoom(HotelRoom room) {
        UUID createdRoom;
        try {
            createdRoom = roomManager.addRoom(room);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.created(URI.create("/room/" + createdRoom)).build();
    }

    //UPDATE\\
    @PostMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity updateRoom(@PathVariable("id") String id, HotelRoom room) {
        try {
            roomManager.updateRoom(roomManager.getRoomById(UUID.fromString(id)), room);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResourceAllocated resourceAllocated) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resourceAllocated.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    //DELETE\\
    @DeleteMapping(value = "/{id}")
    public ResponseEntity removeRoom(@PathVariable("id") String id) {
        try {
            roomManager.removeRoom(UUID.fromString(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResourceAllocated resourceAllocated) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resourceAllocated.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    //READ\\
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<HotelRoom> getRoomById(@PathVariable("id") String id) {
        HotelRoom room = roomManager.getRoomById(UUID.fromString(id));

        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(room);
    }

    @GetMapping(value = "/number/{number}", produces = "application/json")
    public ResponseEntity<HotelRoom> getRoomByNumber(@PathVariable("number") int number) {
        HotelRoom room = roomManager.getRoomByNumber(number);

        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(room);
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<HotelRoom>> getAllRooms() {
        return ResponseEntity.ok(roomManager.giveAllRooms());
    }
}
