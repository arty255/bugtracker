package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dao.util.FieldFilter;
import org.hetsold.bugtracker.dao.util.FilterOperation;
import org.hetsold.bugtracker.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;

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
        User sourceUser = new User.Builder().withNames("Alex", "Test").build();
        userDao.save(sourceUser);
        User resultUser = userDao.getUserByUUID(sourceUser.getUuid());
        assertEquals(sourceUser.getUuid(), resultUser.getUuid());
    }

    @Test
    public void checkIfUsersCountCorrectly() {
        userDao.save(new User.Builder().withNames("First", "User").build());
        userDao.save(new User.Builder().withNames("Second", "User").build());
        //expected added count + 1(added default admin)
        assertEquals(3, userDao.getUsersCount(new Contract(new HashSet<>(), new ArrayList<>())));
    }

    @Test
    public void checkIfUsersCanBeFilteredByContract() {
        Contract contract = new Contract(new HashSet<>(), new ArrayList<>());
        contract.getFilters().add(new FieldFilter("firstName", FilterOperation.LIKE, "ob"));
        userDao.save(new User.Builder().withNames("Alex", "Test").build());
        userDao.save(new User.Builder().withNames("Tom", "Test").build());
        userDao.save(new User.Builder().withNames("Rob", "Test").build());
        userDao.save(new User.Builder().withNames("Bob", "Test").build());
        assertEquals(2, userDao.getUsers(contract, 0, 100).size());
    }
}