package pl.ias.pas.hotelroom.springrest.pas.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;

import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class User {

    @EqualsAndHashCode.Include
    private UUID id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private UserType userType = UserType.CLIENT;
    private boolean isActive = true;

    // nadawanie id to odpowiedzialność repozytorium
    // typ użytkownika to nadaje się potem (kto przy rejestracji od razu wybiera typ użytkownika? xD)
    public User(UUID id, String login, String password, String name, String surname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.userType = UserType.CLIENT;
    }

    public void validateLogin() throws ValidationException {
        if(login.length() < 3 || login.length() > 20)
            throw new ValidationException("Login must be between 3 and 20 characters");
    }

    public void validatePassword() throws ValidationException {
        if(password.length() < 8 || password.length() > 20)
            throw new ValidationException("Password must be between 8 and 20 characters");
    }

    public void validateName() throws ValidationException {
        if(name.length() < 1 )
            throw new ValidationException("Name must be minimum 1 character");
    }

    public void validateSurname() throws ValidationException {
        if(surname.length() < 1 )
            throw new ValidationException("Surname must be minimum 1 character");
    }

    public void validateUserType() throws ValidationException {
        if(userType == null)
            throw new ValidationException("User type must be set");
    }

    public void validate() throws ValidationException {
        validateLogin();
        validatePassword();
        validateName();
        validateSurname();
        validateUserType();
    }
}
