package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserDAO {
    void save(User user);

    void delete(User user);

    User getUserById(String uuid);

    long getUsersCount(Contract contract);

    List<User> getUsers(Contract contract, int first, int limit);
}