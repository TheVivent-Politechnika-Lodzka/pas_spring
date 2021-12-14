package pl.ias.pas.hotelroom.springrest.pas.dao;

import org.springframework.web.context.annotation.ApplicationScope;
import pl.ias.pas.hotelroom.springrest.pas.model.User;


import java.util.*;

@ApplicationScope
public class UserDao {

    private List<User> usersRepository = Collections.synchronizedList(new ArrayList<>());
//    private List<User> archiveRepository = Collections.synchronizedList(new ArrayList<>());

    public UUID addUser(User user) {
        usersRepository.add(user);
        return user.getId();
    }

    public void updateUser(User oldUser, User user) {
        if(user.getLogin() != null) {
            oldUser.setLogin(user.getLogin());
        }
        if(user.getPassword() != null) {
            oldUser.setPassword(user.getPassword());
        }
        if(user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if(user.getSurname() != null) {
            oldUser.setSurname(user.getSurname());
        }
    }

    public void removeUser(User user) {
        getUserById(user.getId()).setActive(false);
    }

    public User getUserById(UUID id) {
        for (User user : usersRepository) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByLogin(String login) {
        for (User user : usersRepository) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    public List<User> searchUsers(String login) {
        ArrayList<User> result = new ArrayList<>();
        String searchLogin = login.toLowerCase(Locale.ROOT);
        for (User user : usersRepository) {
            String currentLogin = user.getLogin().toLowerCase(Locale.ROOT);
            if (currentLogin.contains(searchLogin)) {
                result.add(user);
            }
        }
        return result;
    }

    public List<User> getAllUsers() {
        return usersRepository;
    }

    public List<User> getActiveUsers(){
        ArrayList<User> result = new ArrayList<>();
        for (User user : usersRepository) {
            if(user.isActive()) {
                result.add(user);
            }
        }
        return result;
    }

    public List<User> getArchivedUsers(){
        ArrayList<User> result = new ArrayList<>();
        for (User user : usersRepository) {
            if(!user.isActive()) {
                result.add(user);
            }
        }
        return result;
    }


    public void activateUser(UUID id) {
        getUserById(id).setActive(true);
    }
}
