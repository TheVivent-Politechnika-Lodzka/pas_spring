package pl.ias.pas.hotelroom.spring.rest.model;

import java.util.UUID;

public class Client extends User<Client> {

    public Client(UUID id, String login, String password, String name, String surname) {
        super(id, login, password, name, surname);
    }

    public Client(Client user) {
        super(user);
    }

    @Override
    public Client copy() {
        return new Client(this);
    }

    @Override
    public int getPermissionLevel() {
        return 1000;
    }
}
