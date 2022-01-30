package pl.ias.pas.hotelroom.spring.rest.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceAllocatedException;
import pl.ias.pas.hotelroom.spring.rest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.spring.rest.model.HotelRoom;


import java.util.List;
import java.util.UUID;


@RequestScope
@Component
public class HotelRoomManager {

    @Autowired
    private HotelRoomDao roomDao;

    public HotelRoom getRoomByNumber(int number) {
        return roomDao.getRoomByNumber(number);
    }

    public HotelRoom getRoomById(UUID id) {
        HotelRoom room = roomDao.getRoomById(id);
        if (room.isActive() == false) {
            throw new ResourceNotFoundException("Room with id " + id + " not found");
        }
        return room;
    }

    public UUID addRoom(HotelRoom room) {
        return roomDao.addHotelRoom(room);
    }

    public void deleteRoom(UUID roomId) {
        HotelRoom room = roomDao.getRoomById(roomId);

        if(room.isAllocated()) {
            throw new ResourceAllocatedException("Room is already allocated");
        }

        roomDao.deleteRoom(roomId);
    }

    public void updateRoom(UUID roomToUpdate, HotelRoom update) {

        if (getRoomById(roomToUpdate).isAllocated())  {
            throw new ResourceAllocatedException("Room is already allocated");
        }

        // TODO jakaś walidacja danych

        roomDao.updateHotelRoom(roomToUpdate, update);

    }

    public List<HotelRoom> getAllRooms() {
        return  roomDao.getAllRooms();
    }

}
