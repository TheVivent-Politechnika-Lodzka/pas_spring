package pl.ias.pas.hotelroom.spring.rest.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ias.pas.hotelroom.spring.rest.managers.UserManager;
import pl.ias.pas.hotelroom.spring.rest.model.Client;
import pl.ias.pas.hotelroom.spring.rest.model.ResourceAdmin;
import pl.ias.pas.hotelroom.spring.rest.model.User;
import pl.ias.pas.hotelroom.spring.rest.model.UserAdmin;

import javax.validation.Valid;
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

    @GetMapping("/hello")
    public String test(){
        System.out.println("########################");
        System.out.println("hello");
        System.out.println("########################");
        return "Hello";
    }

    //CREATE\\
    @PostMapping(value="/create", consumes = "application/json")
    public ResponseEntity createUser(@Valid @RequestBody Client user) {
        UUID createdUser = userManager.addUser(user);

        return ResponseEntity.created(URI.create("/user/" + createdUser)).build();
    }

    @PostMapping(value="/createUserAdmin", consumes = "application/json")
    public ResponseEntity createUser(@Valid @RequestBody UserAdmin user) {
        UUID createdUser = userManager.addUser(user);

        return ResponseEntity.created(URI.create("/user/" + createdUser)).build();
    }

    @PostMapping(value="/createResourceAdmin", consumes = "application/json")
    public ResponseEntity createUser(@Valid @RequestBody ResourceAdmin user) {
        UUID createdUser = userManager.addUser(user);

        return ResponseEntity.created(URI.create("/user/" + createdUser)).build();
    }


    //UPDATE\\
    @PostMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity updateUser(@PathVariable("id") String userToUpdate, @RequestBody User update) {
        UUID id = UUID.fromString(userToUpdate);
        userManager.updateUser(id, update);

        return ResponseEntity.ok().build();
    }

    //ACTIVATE\\
    @RequestMapping(value = "/activate/{id}", method = RequestMethod.HEAD)
    public ResponseEntity activateUser(@PathVariable("id") String id) {
        userManager.activateUser(UUID.fromString(id));

        return ResponseEntity.ok().build();
    }

    //ARCHIVE\\
    @RequestMapping(value = "/archive/{id}")
    public ResponseEntity archiveUser(@PathVariable("id") String id) {
        userManager.archiveUser(UUID.fromString(id));

        return ResponseEntity.ok().build();
    }

    //READ\\
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        User user = userManager.getUserById(UUID.fromString(id), false);

        return ResponseEntity.ok(user);
    }


    @GetMapping(value = "/search/{login}", produces = "application/json")
    public ResponseEntity<List<User>> getUsersContainsLogin(@PathVariable("login") String login) {
        return ResponseEntity.ok(userManager.searchUsers(login));
    }

    @GetMapping(value = "/login/{login}", produces = "application/json")
    public ResponseEntity<User> getUserByLogin(@PathVariable("login") String login) {
        User user = userManager.getUserByLogin(login);

        return ResponseEntity.ok(user);
    }


    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false, value = "scope") String scope) {
        if (scope == null || scope.isEmpty()) scope = "active";
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
