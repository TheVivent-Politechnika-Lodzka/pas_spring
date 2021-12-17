package pl.ias.pas.hotelroom.springrest.pas.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pl.ias.pas.hotelroom.springrest.pas.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.springrest.pas.dao.ReservationDao;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAllocatedException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;
import pl.ias.pas.hotelroom.springrest.pas.model.HotelRoom;


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
        return roomDao.getRoomById(id);
    }

    public UUID addRoom(HotelRoom room) {
        return roomDao.addHotelRoom(room);
    }

    public void archiveRoom(UUID roomId) {
        HotelRoom room = roomDao.getRoomById(roomId);

        if(room.isAllocated()) {
            throw new ResourceAllocatedException("Room is already allocated");
        }

        roomDao.archiveRoom(roomId);
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
