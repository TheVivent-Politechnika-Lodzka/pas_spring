package pl.ias.pas.hotelroom.spring.rest.tests.unit.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ias.pas.hotelroom.spring.rest.dao.HotelRoomDao;
import pl.ias.pas.hotelroom.spring.rest.model.HotelRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HotelRoomDaoTest {

    HotelRoomDao hotelRoomDao;
    List<UUID> roomIds;

    @BeforeEach
    public void setUp() {
        hotelRoomDao = new HotelRoomDao();
        roomIds = new ArrayList<>(3);

        HotelRoom hotelRoom1 = new HotelRoom(UUID.randomUUID(),1, 100, 10, "Pierwszy pokój");
        HotelRoom hotelRoom2 = new HotelRoom(UUID.randomUUID(),2, 200, 20, "Drugi pokój");
        HotelRoom hotelRoom3 = new HotelRoom(UUID.randomUUID(),3, 300, 30, "Trzeci pokój");

        roomIds.add(hotelRoomDao.addHotelRoom(hotelRoom1));
        roomIds.add(hotelRoomDao.addHotelRoom(hotelRoom2));
        roomIds.add(hotelRoomDao.addHotelRoom(hotelRoom3));
        }



    @Test
    public void getReturnsCopy() {
        HotelRoom hotelRoom = hotelRoomDao.getRoomById(roomIds.get(0));

        hotelRoom.setDescription(hotelRoom.getDescription() + " - nowa opis");
        hotelRoom.setRoomNumber(hotelRoom.getRoomNumber() + 1);
        hotelRoom.setPrice(hotelRoom.getPrice() + 1);
        hotelRoom.setCapacity(hotelRoom.getCapacity() + 1);

        HotelRoom hotelRoom2 = hotelRoomDao.getRoomById(roomIds.get(0));
        assertNotEquals(hotelRoom.getDescription(), hotelRoom2.getDescription());
        assertNotEquals(hotelRoom.getRoomNumber(), hotelRoom2.getRoomNumber());
        assertNotEquals(hotelRoom.getPrice(), hotelRoom2.getPrice());
        assertNotEquals(hotelRoom.getCapacity(), hotelRoom2.getCapacity());

    }


}
