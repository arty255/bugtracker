package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.dao.util.Contract;

import java.util.List;

public interface UserDAO {
    void save(User user);

    void delete(User user);

    User getUserById(String uuid);

    long getUsersCount(Contract contract);

    List<User> getUsers(Contract contract, int first, int limit);
}