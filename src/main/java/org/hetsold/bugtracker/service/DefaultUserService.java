package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.facade.UserConvertor;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class DefaultUserService implements UserService {
    private UserDAO userDAO;

    public DefaultUserService() {
    }

    @Autowired
    public DefaultUserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(User user) {
        if (user == null || user.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect user");
        }
        userDAO.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User getUserById(User user) {
        if (user == null || user.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect user");
        }
        return userDAO.getUserById(user.getUuid());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserDTO getUserById(String uuid) {
        if (uuid.isEmpty()) {
            throw new IllegalArgumentException("incorrect user");
        }
        return UserConvertor.getUserDTO(getUserById(new User(uuid)));
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userDAO.listAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsers(int first, int count) {
        return userDAO.getUsers(first, count).stream().map(UserConvertor::getUserDTO).collect(Collectors.toList());
    }

    @Override
    public long getUserCount() {
        return userDAO.getUsersCount();
    }
}