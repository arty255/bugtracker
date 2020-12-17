package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class DefaultUserService implements UserService {
    private UserDAO userDAO;

    public DefaultUserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    public User getUser(String userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }
}
