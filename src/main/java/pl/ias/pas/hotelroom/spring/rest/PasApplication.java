package pl.ias.pas.hotelroom.spring.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
