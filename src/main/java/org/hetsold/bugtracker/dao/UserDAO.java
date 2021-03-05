package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.model.User;

import java.util.List;
import java.util.UUID;

public interface UserDAO {
    void save(User user);

    void delete(User user);

    User getUserByUUID(UUID uuid);

    long getUsersCount(Contract contract);

    List<User> getUsers(Contract contract, int first, int limit);
}