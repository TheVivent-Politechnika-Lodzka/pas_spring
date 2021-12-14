package pl.ias.pas.hotelroom.pasrest.managers;

import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.IDontKnowException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceAlreadyExistException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ResourceNotFoundException;
import pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename.ValidationException;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@RequestScoped
public class UserManager {


    @Inject
    private UserDao userDao;

    public UserManager() {}

    public User getUserByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    public List<User> searchUsers(String login) {
        return userDao.searchUsers(login);
    }

    public List<User> getAllUsers(){
        return userDao.getAllUsers();
    }

    public List<User> getAllActiveUsers() {
        return userDao.getActiveUsers();
    }

    public List<User> getAllArchivedUsers() {
        return userDao.getArchivedUsers();
    }

    public User getUserById(UUID id, boolean includeArchived){
        for (User user : userDao.getActiveUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }

        // jeśli nie ma użytkownika w bazie, to sprawdzamy czy może jest w archiwum
        if (! includeArchived) return null;
        for (User user : userDao.getArchivedUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }

        return null;
    }

    public UUID addUser(User user) throws ValidationException, ResourceAlreadyExistException, IDontKnowException {


        // waliduj wszystko
        user.validate();

        // sprawdzanie unikalności loginu i id
        UUID id = UUID.randomUUID();
        for (User currentUser : getAllUsers()) {
            if (currentUser.getLogin().equals(user.getLogin())) {
                throw new ResourceAlreadyExistException("User with this login already exists");
            }
            if (currentUser.getId().equals(id)) {
                throw new IDontKnowException("ID error, please try again");
            }
        }

        // stworzenie nowego użytkownika
        User newUser = new User(id, user.getLogin(), user.getPassword(), user.getName(), user.getSurname());

        return userDao.addUser(newUser);
    }

    public void removeUser(UUID id) throws ResourceNotFoundException {
        User user = userDao.getUserById(id);
        //czy uzytkownik jest w bazie
        if (!userDao.getActiveUsers().contains(user)) {
            throw new ResourceNotFoundException("User does not exist");
        }

        userDao.removeUser(user);
    }

    public void activateUser(UUID id) throws ResourceNotFoundException {
        User user = userDao.getUserById(id);
        //czy uzytkownik jest w bazie
        if (user == null) {
            throw new ResourceNotFoundException("User does not exist");
        }

        userDao.activateUser(id);
    }

    public void updateUser(UUID oldUserId, User user) throws ResourceNotFoundException, ValidationException {

        User oldUser = userDao.getUserById(oldUserId);
        if (oldUser == null) {
            throw new ResourceNotFoundException("User does not exist");
        }

        if (user.getLogin() != null) {
            user.validateLogin();
        }
        if (user.getPassword() != null) {
            user.validatePassword();
        }
        if (user.getName() != null) {
            user.validateName();
        }
        if (user.getSurname() != null) {
            user.validateSurname();
        }

        userDao.updateUser(oldUser, user);
    }

}
