package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.User;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", "mock"})
public class DefaultUserServiceTest {
    private static User user;
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserService userService;

    @BeforeClass
    public static void prepareData() {
        user = new User("Oleg", "Sinevski");
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void checkIfUserSaved() {
        userService.save(user);
        Mockito.verify(userDAO, Mockito.times(1)).save(user);
    }

    @Test
    public void checkIfUserCanBeDeleted() {
        userService.delete(user);
        Mockito.verify(userDAO, Mockito.times(1)).delete(user);
    }
}