package pl.ias.pas.hotelroom.springrest.pas.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.IDontKnowException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.springrest.pas.exceptions.ValidationException;
import pl.ias.pas.hotelroom.springrest.pas.managers.UserManager;
import pl.ias.pas.hotelroom.springrest.pas.model.User;

import java.net.URI;
import java.util.List;
import java.util.UUID;


//@RequestScope
@RestController
@RequestMapping("/user")
public class UserEndpoint {

    @Autowired
    private UserManager userManager;

    // przykładowe zapytanie tworzące nowego użytkownika
    // http POST localhost:8080/PASrest-1.0-SNAPSHOT/api/user login=test password=test name=test surname=test

    @GetMapping("/")
    public String test(){
        return "test";
    }

    //CREATE\\
    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity createUser(User user) {
        UUID createdUser;
        try {
            createdUser = userManager.addUser(user);
        }catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IDontKnowException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.created(URI.create("/user/" + createdUser)).build();
    }

    //UPDATE\\
    @PostMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity updateUser(@PathVariable("id") String id, User user) {
        try {
            UUID oldUser = UUID.fromString(id);
            userManager.updateUser(oldUser, user);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/activate/{id}", method = RequestMethod.HEAD)
    public ResponseEntity activateUser(@PathVariable("id") String id) {
        try {
            userManager.activateUser(UUID.fromString(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    //DELETE\\
    @DeleteMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity archiveUser(@PathVariable("id") String id) {
        try {
            userManager.removeUser(UUID.fromString(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    //READ\\
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        User user = userManager.getUserById(UUID.fromString(id), false);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(user);
    }


    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<List<User>> getUsersContainsLogin(@PathVariable("login") String login) {
        return ResponseEntity.ok(userManager.searchUsers(login));

    }

    @GetMapping(value = "/login/{login}", produces = "application/json")
    public ResponseEntity<User> getUserByLogin(@PathVariable("login") String login) {
        User user = userManager.getUserByLogin(login);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);

    }


    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<?> getAllUsers(@RequestParam("scope") String scope) {
        if (scope == null) scope = "active";
        List<User> toReturn;
        switch (scope) {
            case "active":
                toReturn = userManager.getAllActiveUsers();
                break;
            case "archived":
                toReturn = userManager.getAllArchivedUsers();
                break;
            case "all":
                toReturn = userManager.getAllUsers();
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong scope");
        }

        return ResponseEntity.ok(toReturn);
    }

}
