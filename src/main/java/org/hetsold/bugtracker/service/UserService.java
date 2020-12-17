package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.User;

public interface UserService {
    void save(User user);

    User getUser(String userId);

    void delete(User user);
}
