package pl.ias.pas.hotelroom.spring.rest.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pl.ias.pas.hotelroom.spring.rest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.spring.rest.dao.UserDao;
import pl.ias.pas.hotelroom.spring.rest.model.User;



import java.util.List;
import java.util.UUID;

@RequestScope
@Component
public class UserManager {


    @Autowired
    private UserDao userDao;

    public UserManager() {}

    public User getUserByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    public List<User> searchUsers(String login) {
        return userDao.customSearch(
                (user) -> user.getLogin().toLowerCase().contains(login.toLowerCase())
        );
    }

    public List<User> getAllUsers(){
        return userDao.getAllUsers();
    }

    public List<User> getAllActiveUsers() {
        return userDao.customSearch((user)-> user.isActive());
    }

    public List<User> getAllArchivedUsers() {
        return userDao.customSearch((user)-> !user.isActive());
    }

    public User getUserById(UUID id, boolean includeArchived){
        User user = userDao.getUserById(id);

        if ( !includeArchived && !user.isActive() ) {
            throw new ResourceNotFoundException("User does not exist");
        }

        return user;
    }

    public UUID addUser(User user) {
        return userDao.addUser(user);
    }

    public void deactivateUser(UUID id) {
        userDao.deactivateUser(id);
    }

    public void activateUser(UUID id) {
        userDao.activateUser(id);
    }

    public void updateUser(UUID userToUpdate, User update) {

        if (update.getLogin() != null) {
            update.validateLogin();
        }
        if (update.getPassword() != null) {
            update.validatePassword();
        }
        if (update.getName() != null) {
            update.validateName();
        }
        if (update.getSurname() != null) {
            update.validateSurname();
        }

        userDao.updateUser(userToUpdate, update);
    }

}
