package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, org.hetsold.bugtracker.TestAppConfig.class})
@ActiveProfiles("test")
@Transactional
public class UserHibernateDAOTest {
    @Autowired
    private UserDAO userDao;

    @Test
    public void checkIfUserCanBeSaved() {
        User user = new User("t1", "t1");
        userDao.save(user);
        User resultUser = userDao.getUserById(user.getUuid());
        assertEquals(user.getUuid(), resultUser.getUuid());
        assertEquals(user.getFirstName(), resultUser.getFirstName());
        assertEquals(user.getLastName(), user.getLastName());
    }

    @Test
    public void checkIfUserCanBeDeleted() {
        User user = new User("t1", "t1");
        userDao.save(user);
        userDao.delete(user);
        List<User> resultList = userDao.listAll();
        assertEquals(0, resultList.size());
    }

    @Test
    public void checkIfUserCanBeFoundById() {
        User sourceUser = new User("t1", "t1");
        userDao.save(sourceUser);
        User resultUser = userDao.getUserById(sourceUser.getUuid());
        assertEquals(sourceUser.getUuid(), resultUser.getUuid());
        assertEquals(sourceUser.getFirstName(), resultUser.getFirstName());
        assertEquals(sourceUser.getLastName(), resultUser.getLastName());
    }

    @Test
    public void checkIfUserCorrectCount() {
        userDao.save(new User("t1", "t1"));
        userDao.save(new User("t2", "t2"));
        assertEquals(2, userDao.getUsersCount());
    }
}