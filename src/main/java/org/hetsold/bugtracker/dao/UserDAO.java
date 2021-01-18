package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.User;

import java.util.List;

public interface UserDAO {
    void save(User user);

    List<User> listAll();

    void delete(User user);

    User getUserById(String uuid);

    long getUsersCount();

    List<User> getUsers(int first, int count);
}
