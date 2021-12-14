package pl.ias.pas.hotelroom.springrest.pas.model;

public enum UserType {

    USER_ADMIN(0),
    DATA_OPERATOR(100),
    CLIENT(1000);

    private int accessLevel;

    UserType(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getAccessLevel() {
        return accessLevel;
    }
}
