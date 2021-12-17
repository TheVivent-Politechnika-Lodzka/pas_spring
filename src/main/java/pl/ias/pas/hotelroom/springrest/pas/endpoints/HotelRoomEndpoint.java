package pl.ias.pas.hotelroom.springrest.pas.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAllocatedException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;
import pl.ias.pas.hotelroom.springrest.pas.managers.HotelRoomManager;
import pl.ias.pas.hotelroom.springrest.pas.model.HotelRoom;

import javax.validation.Valid;
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
    @PostMapping(consumes = "application/json")
    public ResponseEntity createRoom(@Valid @RequestBody HotelRoom room) {
        UUID createdRoom = roomManager.addRoom(room);
        return ResponseEntity.created(URI.create("/room/" + createdRoom)).build();
    }

    //UPDATE\\
    @PostMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity updateRoom(@PathVariable("id") String roomToUpdate, @RequestBody HotelRoom update) {
        UUID id = UUID.fromString(roomToUpdate);
        roomManager.updateRoom(id, update);

        return ResponseEntity.ok().build();
    }

    //DELETE\\
    @DeleteMapping(value = "/{id}")
    public ResponseEntity removeRoom(@PathVariable("id") String id) {
        roomManager.archiveRoom(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    //READ\\
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<HotelRoom> getRoomById(@PathVariable("id") String id) {
        HotelRoom room = roomManager.getRoomById(UUID.fromString(id));

        return ResponseEntity.ok(room);
    }

    @GetMapping(value = "/number/{number}", produces = "application/json")
    public ResponseEntity<HotelRoom> getRoomByNumber(@PathVariable("number") int number) {
        HotelRoom room = roomManager.getRoomByNumber(number);

        return ResponseEntity.ok(room);
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<HotelRoom>> getAllRooms() {
        return ResponseEntity.ok(roomManager.getAllRooms());
    }
}
