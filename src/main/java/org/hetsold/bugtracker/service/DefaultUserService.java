package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class DefaultUserService implements UserService {
    private UserDAO userDAO;

    public DefaultUserService() {
    }

    public DefaultUserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void save(User user) {
        if (user == null || user.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect user");
        }
        userDAO.save(user);
    }

    public User getUser(String userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    public User getUserById(User user) {
        if (user == null || user.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect user");
        }
        return userDAO.getUserById(user.getUuid());
    }

    @Override
    public User getUserById(User user) {
        if (user == null || user.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect user");
        }
        return userDAO.getUserById(user.getUuid());
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }


}
