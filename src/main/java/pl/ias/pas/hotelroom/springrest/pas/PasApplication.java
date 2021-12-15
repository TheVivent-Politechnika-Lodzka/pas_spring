package pl.ias.pas.hotelroom.springrest.pas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import pl.ias.pas.hotelroom.springrest.pas.managers.HotelRoomManager;


@SpringBootApplication
//        (
//        scanBasePackages = {"pl.ias.pas.hotelroom.springrest.pas.managers", "pl.ias.pas.hotelroom.springrest.pas.endpoints"}
//)
public class PasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasApplication.class, args);
    }

}
