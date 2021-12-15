package pl.ias.pas.hotelroom.springrest.pas.dao;



import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.ias.pas.hotelroom.springrest.pas.model.HotelRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScope
@Component
public class HotelRoomDao {

    private List<HotelRoom> roomsRepository = Collections.synchronizedList(new ArrayList<>());

    public UUID addHotelRoom(HotelRoom room) {
        roomsRepository.add(room);
        return room.getId();
    }

    public void updateHotelRoom(HotelRoom oldRoom, HotelRoom room) {
        if(room.getRoomNumber() > 0) {
            oldRoom.setRoomNumber(room.getRoomNumber());
        }
        if(room.getPrice() > 0) {
            oldRoom.setPrice(room.getPrice());
        }
        if(room.getCapacity() > 0) {
            oldRoom.setCapacity(room.getCapacity());
        }
        if(room.getDescription() != null) {
            oldRoom.setDescription(room.getDescription());
        }
    }

    public void removeRoom(HotelRoom room) {
        roomsRepository.remove(room);
    }

    public HotelRoom getRoomById(UUID id) {
        for (HotelRoom room : roomsRepository) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        return null;
    }

    public HotelRoom getRoomByNumber(int number) {
        for (HotelRoom room : roomsRepository) {
            if (room.getRoomNumber() == number) {
                return room;
            }
        }
        return null;
    }

    public List<HotelRoom> getAllRooms() {
        return roomsRepository;
    }
}
