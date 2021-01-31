package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.facade.UserMapper;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.UserDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"dev", ""})
public class DefaultUserServiceTest {
    private User userEntity;
    private UserDTO userDTO;
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private final UserService userService = new DefaultUserService();

    @Before
    public void beforeTest() {
        MockitoAnnotations.openMocks(this);
        userEntity = new User("Oleg", "Tester");
        userDTO = UserMapper.getUserDTO(userEntity);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void checkIfUserCanBeRegisteredAndReceiveCorrectDTO() {
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        UserDTO savedUser = userService.registerUser(userDTO);
        Mockito.verify(userDAO, Mockito.times(1)).save(userArgumentCaptor.capture());
        assertEquals(savedUser.getFirstName(), userEntity.getFirstName());
        assertEquals(savedUser.getLastName(), userEntity.getLastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfUserWithBlankFirstAndLastNamesThrowException() {
        userDTO.setFirstName("");
        userDTO.setLastName("");
        userService.registerUser(userDTO);
    }

    @Test
    public void checkIfUserCanBeUpdated(){
        Mockito.when(userDAO.getUserById(userEntity.getUuid())).thenReturn(userEntity);
        userDTO.setLastName("changed");
        userService.updateUser(userDTO);
        assertEquals(userEntity.getUuid(), userDTO.getUuid());
        assertEquals(userEntity.getLastName(), userDTO.getLastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfEmptyFirstAndLastNameOnUpdateThrowsException(){
        userDTO.setLastName("");
        userDTO.setFirstName("");
        userService.updateUser(userDTO);
    }

    @Test
    public void checkIfUserCanBeDeleted() {
        Mockito.when(userDAO.getUserById(userEntity.getUuid())).thenReturn(userEntity);
        userService.delete(userEntity.getUuid());
        Mockito.verify(userDAO, Mockito.times(1)).delete(userEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfEmptyUserUUIDOnDeleteThrowsException() {
        userService.delete("");
    }

    @Test
    public void checkIfGetByUUIDReturnUser() {
        Mockito.when(userDAO.getUserById(userEntity.getUuid())).thenReturn(userEntity);
        User resultUser = userService.getUserById(userEntity.getUuid());
        Mockito.verify(userDAO).getUserById(userEntity.getUuid());
        assertEquals(resultUser.getUuid(), userEntity.getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfGetUserWithEmptyIdThrowException() {
        User emptyIdUser = new User("test", "test");
        emptyIdUser.setUuid("");
        userService.getUserById(emptyIdUser.getUuid());
    }
}