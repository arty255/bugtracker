package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.User;

public interface UserService {
    void save(User user);

    User getUserById(User user);

    void delete(User user);
}
