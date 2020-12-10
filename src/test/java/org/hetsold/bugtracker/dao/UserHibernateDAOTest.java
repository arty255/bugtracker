package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.User;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, org.hetsold.bugtracker.TestAppConfig.class})
@ActiveProfiles("test")
@Transactional
public class UserHibernateDAOTest {
    @Autowired
    private UserDAO userDao;

    private static List<User> userList = new ArrayList<>();

    @BeforeClass
    public static void prepareData() {
        for (int i = 1; i < 5; i++) {
            userList.add(new User("f_name_" + i, "f_name_" + i));
        }
    }

    @Test
    public void checkIfUserCanBeSaved() {
        User user = userList.get(0);
        userDao.save(user);
        User resultUser = userDao.getUserById(user.getUuid());
        assertEquals(user.getUuid(), resultUser.getUuid());
        assertEquals(user.getFirstName(), resultUser.getFirstName());
        assertEquals(user.getLastName(), user.getLastName());
    }

    @Test
    public void checkIfUserCanBeDeleted() {
        userDao.save(userList.get(0));
        userDao.delete(userList.get(0));
        List<User> resultList = userDao.listAll();
        assertEquals(resultList.size(), 0);
    }

    @Test
    public void checkIfUserCanBeFoundById() {
        User sourceUser = userList.get(0);
        userDao.save(sourceUser);
        User resultUser = userDao.getUserById(sourceUser.getUuid());
        assertEquals(sourceUser.getUuid(), resultUser.getUuid());
        assertEquals(sourceUser.getFirstName(), resultUser.getFirstName());
        assertEquals(sourceUser.getLastName(), resultUser.getLastName());
    }

    @Test
    public void checkIfUserCorrectCount() {
        userList.forEach(item -> userDao.save(item));
        assertEquals(userDao.getUsersCount(), userList.size());
    }
}