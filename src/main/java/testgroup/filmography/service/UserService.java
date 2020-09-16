package testgroup.filmography.service;

import testgroup.filmography.model.User;

import java.util.List;

public interface UserService {

    void createUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);

    User getUserByName(String name);

    void updateUser(User user);

    void deleteUser(long id);
}
