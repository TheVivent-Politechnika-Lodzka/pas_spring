package pl.ias.pas.hotelroom.spring.rest.dao;



import com.pushtorefresh.javac_warning_annotation.Warning;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.spring.rest.model.HotelRoom;

import java.util.*;
import java.util.function.Predicate;

@ApplicationScope
@Repository
public class HotelRoomDao {

//    private List<HotelRoom> roomsRepository = new ArrayList<HotelRoom>();
    private Map<UUID, HotelRoom> roomsById = new HashMap<>();
    private Map<Integer, HotelRoom> roomsByNumber = new HashMap<>();

    @Warning("This method is for testing only !!!")
    synchronized public void deleteRoom(UUID roomId) {
        if(roomsById.containsKey(roomId)){
            roomsByNumber.remove(
                    roomsById.get(roomId).getRoomNumber()
            );
            roomsById.remove(roomId);
        }
        else {
            throw new ResourceNotFoundException("Room with id: " + roomId + " not found");
        }
    }

    synchronized HotelRoom getActualRoom(UUID roomId) {
        HotelRoom room = roomsById.get(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room with id " + roomId + " not found");
        }
        return new HotelRoom(room);
    }

    synchronized public UUID addHotelRoom(HotelRoom room) {
        UUID id = UUID.randomUUID();

        //wykonaj kopię pokoju, aby nie były możliwe zmiany w obiekcie
        HotelRoom newRoom = new HotelRoom(id, room.getRoomNumber(), room.getPrice(), room.getCapacity(), room.getDescription());

        if (roomsById.containsKey(newRoom.getId())) {
            throw new ResourceAlreadyExistException("Room with id " + newRoom.getId() + " already exist");
        }
        if (roomsByNumber.containsKey(newRoom.getRoomNumber())) {
            throw new ResourceAlreadyExistException("Room with number " + newRoom.getRoomNumber() + " already exist");
        }

        roomsById.put(newRoom.getId(), newRoom);
        roomsByNumber.put(newRoom.getRoomNumber(), newRoom);
        return newRoom.getId();
    }

    synchronized public void updateHotelRoom(UUID roomToUpdate, HotelRoom update) {
        HotelRoom room = roomsById.get(roomToUpdate);

        if (room == null) {
            throw new ResourceNotFoundException("Room with id " + roomToUpdate + " not found");
        }
        if (roomsByNumber.containsKey(update.getRoomNumber())) {
            throw new ResourceAlreadyExistException("Room with number " + update.getRoomNumber() + " already exist");
        }

        if(update.getRoomNumber() > 0) {
            // usuń pokój z mapy pokojów po numerze
            roomsByNumber.remove(room.getRoomNumber());

            // ustaw nowy numer pokoju
            room.setRoomNumber(update.getRoomNumber());
            // dodaj pokój do mapy pokojów po numerze
            roomsByNumber.put(update.getRoomNumber(), room);
        }
        if(update.getPrice() > 0) {
            room.setPrice(update.getPrice());
        }
        if(update.getCapacity() > 0) {
            room.setCapacity(update.getCapacity());
        }
        if(update.getDescription() != null) {
            room.setDescription(update.getDescription());
        }

    }

    synchronized public void archiveRoom(UUID roomId) {
        if (roomsById.containsKey(roomId)) {
            roomsById.get(roomId).setActive(false);
        } else {
            throw new ResourceNotFoundException("Room with id " + roomId + " not found");
        }
    }

    synchronized public HotelRoom getRoomById(UUID id) {
        HotelRoom room = roomsById.get(id);
        if (room == null) {
            throw new ResourceNotFoundException("Room with id " + id + " not found");
        }
        return new HotelRoom(room);
    }

    synchronized public HotelRoom getRoomByNumber(int number) {
        HotelRoom room = roomsByNumber.get(number);
        if (room == null) {
            throw new ResourceNotFoundException("Room with number " + number + " not found");
        }
        return new HotelRoom(room);
    }

    synchronized public List<HotelRoom> getAllRooms() {
        List<HotelRoom> rooms = new ArrayList<HotelRoom>(roomsById.size());
        roomsById.forEach((key, room) -> rooms.add(new HotelRoom(room)));
        return rooms;
    }

    synchronized public List<HotelRoom> customSearch(Predicate<HotelRoom> lambda) {
        List<HotelRoom> rooms = new ArrayList<HotelRoom>(roomsById.size());
        roomsById.forEach((key, room) -> {
            if(lambda.test(room)) {
                rooms.add(new HotelRoom(room));
            }
        });
        return rooms;
    }


}
