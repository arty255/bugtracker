package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.User;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", "mock"})
public class UserServiceTest {
    private static User user;
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserService userService;

    @BeforeClass
    public static void prepareData() {
        user = new User("Oleg", "Sinevski");
    }

    @Test
    public void checkIfUserSaved() {
        userService.save(user);
        Mockito.verify(userDAO).save(user);
    }

    @Test
    public void checkIfUserDeleted() {
        userService.delete(user);
        Mockito.verify(userDAO).delete(user);
    }
}