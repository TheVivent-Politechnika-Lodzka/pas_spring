package pl.ias.pas.hotelroom.springrest.pas.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;
import pl.ias.pas.hotelroom.springrest.pas.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.springrest.pas.dao.ReservationDao;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAllocated;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;
import pl.ias.pas.hotelroom.springrest.pas.model.HotelRoom;


import java.util.List;
import java.util.UUID;


@RequestScope

public class HotelRoomManager {

    @Autowired
    private HotelRoomDao roomDao;
    @Autowired
    private ReservationDao reservationDao;

    public HotelRoom getRoomByNumber(int number) {
        return roomDao.getRoomByNumber(number);
    }

    public HotelRoom getRoomById(UUID id) {
        return roomDao.getRoomById(id);
    }

    public UUID addRoom(HotelRoom room) throws ResourceAlreadyExistException, ValidationException {
        UUID id = UUID.randomUUID();

        // sprawdzanie unikalności numeru
        if (roomDao.getRoomByNumber(room.getRoomNumber()) != null) {
            throw new ResourceAlreadyExistException("Room already exists");
        }

        // walidacja danych
        room.validate();

        // wstawienie nowego pokoju
        HotelRoom newRoom = new HotelRoom(id, room.getRoomNumber(), room.getPrice(), room.getCapacity(), room.getDescription());
        return roomDao.addHotelRoom(newRoom);
    }

    public void removeRoom(UUID id) throws ResourceAllocated, ResourceNotFoundException {
        HotelRoom room = roomDao.getRoomById(id);

        if(room.isAllocated()) {
            throw new ResourceAllocated("Room is already allocated");
        }

        //sprawdzenie czy pokoj jest w bazie
        if (roomDao.getRoomById(id) == null) {
            throw new ResourceNotFoundException("Room does not exist");
        }

        roomDao.removeRoom(room);
    }

    public void updateRoom(HotelRoom old, HotelRoom room) throws ResourceNotFoundException, ResourceAllocated, ValidationException {
        if (roomDao.getRoomById(old.getId()) == null) {
            throw new ResourceNotFoundException("Room doesn't exist");
        }

        if (old.isAllocated())  {
            throw new ResourceAllocated("Room is already allocated");
        }

        if(old.getCapacity() != 0) {
            old.validateCapacity();
        }
        if(old.getPrice() != 0) {
            old.validatePrice();
        }
        if (old.getRoomNumber() != 0) {
            old.validateRoomNumber();
        }
        if (old.getDescription() != null) {
            old.validateDescription();
        }

        roomDao.updateHotelRoom(old, room);

    }

    public List<HotelRoom> giveAllRooms() {
        return  roomDao.getAllRooms();
    }

}
