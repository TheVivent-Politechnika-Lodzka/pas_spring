package pl.ias.pas.hotelroom.spring.rest.dao;

import com.pushtorefresh.javac_warning_annotation.Warning;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.spring.rest.model.User;


import java.util.*;
import java.util.function.Predicate;

@ApplicationScope
@Repository
public class UserDao {

//    private List<User> usersRepository = Collections.synchronizedList(new ArrayList<>());
//    private List<User> archiveRepository = Collections.synchronizedList(new ArrayList<>());
    private Map<UUID, User> usersById = new HashMap<>();
    private Map<String, User> usersByLogin = new HashMap<>();

    @Warning("This method is for testing only !!!")
    synchronized public void deleteUser(UUID id) {
        if (usersById.containsKey(id)) {
            usersByLogin.remove(
                    usersById.get(id).getLogin()
            );
            usersById.remove(id);
        }
        else {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
    }

    synchronized User getActualUser(UUID id) {
        if (usersById.containsKey(id)) {
            return usersById.get(id);
        }
        else {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
    }

    synchronized public UUID addUser(User user) {
        UUID id = UUID.randomUUID();

        //wykonaj kopię obiektu user, aby nie zmieniać obiektu w repozytorium
//        User newUser = new User(id, user.getLogin(), user.getPassword(), user.getName(), user.getSurname());
        User newUser = user.copy();
        newUser.setId(id);


        if(usersById.containsKey(newUser.getId())) {
            throw new IllegalArgumentException("User with id " + newUser.getId() + " already exists");
        }
        if(usersByLogin.containsKey(newUser.getLogin())) {
            throw new IllegalArgumentException("User with login " + newUser.getLogin() + " already exists");
        }

        usersById.put(newUser.getId(), newUser);
        usersByLogin.put(newUser.getLogin(), newUser);
        return newUser.getId();
    }

    synchronized public void updateUser(UUID userToUpdate, User update) {
        User user = usersById.get(userToUpdate);

        if (user == null) {
            throw new ResourceNotFoundException("User with id " + userToUpdate + " does not exist");
        }
        if (!user.getLogin().equals(update.getLogin())
                && usersByLogin.containsKey(update.getLogin())) {
            throw new ResourceAlreadyExistException("User with login " + update.getLogin() + " already exists");
        }

        if(update.getLogin() != null) {
            // usuń starego usera z mapy po loginie
            usersByLogin.remove(user.getLogin());

            // zmień login usera
            user.setLogin(update.getLogin());
            // dodaj zaaktualizowanego usera do mapy po loginie
            usersByLogin.put(user.getLogin(), user);
        }
        if(update.getPassword() != null) {
            user.setPassword(update.getPassword());
        }
        if(update.getName() != null) {
            user.setName(update.getName());
        }
        if(update.getSurname() != null) {
            user.setSurname(update.getSurname());
        }
    }

    synchronized public void archiveUser(UUID user) {
        if (usersById.containsKey(user)) {
            usersById.get(user).setActive(false);
        }
        else {
            throw new ResourceNotFoundException("User with id " + user + " does not exist");
        }
    }

    synchronized public User getUserById(UUID id) {
        User user = usersById.get(id);
        if(user == null) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
        return user.copy();
    }

    synchronized public User getUserByLogin(String login) {
        User user = usersByLogin.get(login);
        if(user == null) {
            throw new ResourceNotFoundException("User with login " + login + " does not exist");
        }
        return user.copy();
    }

    synchronized public List<User> customSearch(Predicate<User> lambda) {
        List<User> result = new ArrayList<>();

        usersById.forEach((key, user) -> {
            if(lambda.test(user)) {
                result.add(user.copy());
            }
        });

//        usersById.entrySet().stream()
//                .filter(
//                    (entry) -> lambda.test(entry.getValue())
//                ).forEach(
//                    entry -> result.add(new User(entry.getValue()))
//                );

        return result;
    }

    synchronized public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        usersById.forEach((key, user) -> result.add(user.copy()));
        return result;
    }

    synchronized public void activateUser(UUID id) {
        if (usersById.containsKey(id)) {
            usersById.get(id).setActive(true);
        } else {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
    }
}
