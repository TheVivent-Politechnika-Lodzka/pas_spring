package pl.ias.pas.hotelroom.springrest.pas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import pl.ias.pas.hotelroom.springrest.pas.managers.HotelRoomManager;
import pl.ias.pas.hotelroom.springrest.pas.managers.ReservationManager;
import pl.ias.pas.hotelroom.springrest.pas.managers.UserManager;


@SpringBootApplication
//        (
//        scanBasePackages = {"pl.ias.pas.hotelroom.springrest.pas"},
//                scanBasePackageClasses = {HotelRoomManager.class, UserManager.class, ReservationManager.class}
//        )
public class PasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasApplication.class, args);
    }

}
