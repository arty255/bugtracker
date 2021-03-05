package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dao.util.FieldFilter;
import org.hetsold.bugtracker.dao.util.FilterOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", ""})
@Transactional
public class UserHibernateDAOTest {
    @Autowired
    private UserDAO userDao;

    @Test
    public void checkIfUserCanBeFoundById() {
        User sourceUser = new User("Alex", "Test");
        userDao.save(sourceUser);
        User resultUser = userDao.getUserByUUID(sourceUser.getUuid());
        assertEquals(sourceUser.getUuid(), resultUser.getUuid());
    }

    @Test
    public void checkIfUsersCountCorrectly() {
        userDao.save(new User("First", "User"));
        userDao.save(new User("Second", "User"));
        assertEquals(2, userDao.getUsersCount(new Contract(null, null)));
    }

    @Test
    public void checkIfUsersCanBeFilteredByContract() {
        Contract contract = new Contract(null, null);
        contract.getFilters().add(new FieldFilter("firstName", FilterOperation.LIKE, "ob"));
        userDao.save(new User("Alex", "Test"));
        userDao.save(new User("Tom", "Test"));
        userDao.save(new User("Rob", "Test"));
        userDao.save(new User("Bob", "Test"));
        assertEquals(2, userDao.getUsers(contract, 0, 100).size());
    }
}