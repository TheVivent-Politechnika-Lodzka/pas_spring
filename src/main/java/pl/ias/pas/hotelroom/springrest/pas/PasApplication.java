package pl.ias.pas.hotelroom.springrest.pas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ias.pas.hotelroom.springrest.pas.managers.HotelRoomManager;
import pl.ias.pas.hotelroom.springrest.pas.managers.ReservationManager;
import pl.ias.pas.hotelroom.springrest.pas.managers.UserManager;


@SpringBootApplication
@RestController
public class PasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasApplication.class, args);
    }

    @RequestMapping
    public String hello() {
        return "Hello World! 😊";
    }

}
