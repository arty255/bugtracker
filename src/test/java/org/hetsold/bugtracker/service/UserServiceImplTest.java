package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.SecurityUserDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.dto.user.FullUserDTO;
import org.hetsold.bugtracker.dto.user.RegistrationDataDTO;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.SecurityUserAuthority;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.service.exception.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"dev", ""})
public class UserServiceImplTest {
    private User savedUser;
    private SecurityUser savedSecurityUser;

    @Mock
    private UserDAO userDAO;
    @Mock
    private SecurityUserDAO securityUserDAO;
    @InjectMocks
    private final UserServiceImpl userService = new UserServiceImpl();

    @Before
    public void beforeTest() {
        MockitoAnnotations.openMocks(this);
        savedUser = new User.Builder().withNames("Oleg", "Tester").build();
        Mockito.when(userDAO.getUserByUUID(savedUser.getUuid())).thenReturn(savedUser);
        savedSecurityUser = new SecurityUser.Builder().withNameAndPassword("unique_login", "password").build();
        savedSecurityUser.setUser(savedUser);
        Mockito.when(securityUserDAO.getSecurityUserByUuid(savedSecurityUser.getUuid())).thenReturn(savedSecurityUser);
        Mockito.when(securityUserDAO.isLoginTaken(savedSecurityUser.getUsername())).thenReturn(true);
        Mockito.when(securityUserDAO.getSecurityUserByUserUuid(savedUser.getUuid())).thenReturn(savedSecurityUser);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerByData_nullDataThrowException() {
        userService.registerByData(null);
    }

    @Test(expected = EmptySecurityUserDataException.class)
    public void registerByData_emptyDataThrowException() {
        RegistrationDataDTO registrationDataDTO = new RegistrationDataDTO("", "", "");
        userService.registerByData(registrationDataDTO);
    }

    @Test(expected = TakenLoginException.class)
    public void registerByData_takenLoginThrowException() {
        RegistrationDataDTO registrationDataDTO = new RegistrationDataDTO(savedSecurityUser.getUsername(), "test11", "Im taken login");
        userService.registerByData(registrationDataDTO);
    }

    @Test
    public void registerByData_correctRegistration() {
        String firstName = "name" + UUID.randomUUID().toString();
        RegistrationDataDTO registrationDataDTO = new RegistrationDataDTO("Login" + UUID.randomUUID().toString(),
                "pass11", firstName);
        userService.registerByData(registrationDataDTO);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userDAO).save(userArgumentCaptor.capture());
        assertEquals(firstName, userArgumentCaptor.getValue().getFirstName());
    }

    @Test(expected = TakenLoginException.class)
    public void registerUser_userWithRegisteredLoginThrowException() {
        User user = new User.Builder().withUUID(null).withNames("testFN", "testLN").build();
        SecurityUser securityUser = new SecurityUser.Builder().withUUID(null).withNameAndPassword(savedSecurityUser.getUsername(), "password").build();
        userService.registerUser(user, securityUser);
    }

    @Test(expected = EmptySecurityUserDataException.class)
    public void registerUser_userWithEmptyUserNameAndPasswordThrowException() {
        User user = new User.Builder().withUUID(null).withNames("testFN", "testLN").build();
        SecurityUser securityUser = new SecurityUser.Builder().withUUID(null).withNameAndPassword("", "").build();
        userService.registerUser(user, securityUser);
    }

    @Test(expected = ContentMismatchException.class)
    public void registerUser_userWithEmptyFirstAndLastNameThrowException() {
        User user = new User.Builder().withUUID(null).build();
        SecurityUser securityUser = new SecurityUser.Builder().withUUID(null).withNameAndPassword("login", "password").build();
        userService.registerUser(user, securityUser);
    }

    @Test
    public void registerUser_correctRegistration() {
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<SecurityUser> securityUserArgumentCaptor = ArgumentCaptor.forClass(SecurityUser.class);
        User user = new User.Builder().withUUID(null).withNames("testFN", "testLN").build();
        SecurityUser securityUser = new SecurityUser.Builder().withUUID(null).withNameAndPassword("login", "password").build();
        userService.registerUser(user, securityUser);
        Mockito.verify(userDAO).save(userArgumentCaptor.capture());
        Mockito.verify(securityUserDAO).save(securityUserArgumentCaptor.capture());
        assertEquals(user.getFirstName(), userArgumentCaptor.getValue().getFirstName());
        assertEquals(securityUser.getUsername(), securityUserArgumentCaptor.getValue().getUsername());
        assertEquals(securityUser.getUser(), user);
    }

    @Test(expected = ContentMismatchException.class)
    public void updateUser_userWithEmptyFirstAndLastNameThrowException() {
        User user = new User.Builder().withUUID(savedUser.getUuid()).build();
        userService.updateUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateUser_notPersistedUserThrowException() {
        User user = new User.Builder().withNames("testFN", "testLN").build();
        Mockito.when(userDAO.getUserByUUID(user.getUuid())).thenReturn(null);
        userService.updateUser(user);
    }

    @Test
    public void updateUser_correctUpdate() {
        User user = new User.Builder().withUUID(savedUser.getUuid()).withNames("testFN", "testLN").build();
        user.setFirstName("edited");
        userService.updateUser(user);
        assertEquals("edited", savedUser.getFirstName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fullUserUpdate_nullSecurityUserThrowException() {
        userService.fullUserUpdate(null);
    }

    @Test(expected = EmptyUUIDKeyException.class)
    public void fullUserUpdate_emptySecurityUserThrowException() {
        FullUserDTO fullUserDTO = new FullUserDTO(savedSecurityUser);
        fullUserDTO.setUuid("");
        userService.fullUserUpdate(fullUserDTO);
    }

    @Test(expected = EmailFormatException.class)
    public void fullUserUpdate_wrongEmailSecurityUserThrowException() {
        FullUserDTO fullUserDTO = new FullUserDTO(savedSecurityUser);
        fullUserDTO.setEmail("dasdfa#fdsds.com");
        userService.fullUserUpdate(fullUserDTO);
    }

    @Test(expected = TakenLoginException.class)
    public void fullUserUpdate_takenLoginThrowException() {
        String newLogin = "newLogin";
        Mockito.when(securityUserDAO.isLoginTaken(newLogin)).thenReturn(true);
        FullUserDTO fullUserDTO = new FullUserDTO(savedSecurityUser);
        fullUserDTO.setEmail("");
        fullUserDTO.setUsername(newLogin);
        userService.fullUserUpdate(fullUserDTO);
    }

    @Test
    public void fullUserUpdate_correctUpdate() {
        String newUserName = "new Name " + UUID.randomUUID().toString();
        User updatedUser = new User.Builder()
                .withUUID(savedUser.getUuid())
                .withNames(newUserName, "test")
                .build();
        String newLogin = "newLogin" + UUID.randomUUID().toString();
        SecurityUser updatedSecurityUser = new SecurityUser.Builder()
                .withUUID(savedSecurityUser.getUuid())
                .withNameAndPassword(newLogin, "123456")
                .withEmail("test@email.com")
                .build();
        updatedSecurityUser.setUser(updatedUser);
        FullUserDTO fullUserDTO = new FullUserDTO(updatedSecurityUser);
        userService.fullUserUpdate(fullUserDTO);
        assertEquals(newLogin, savedSecurityUser.getUsername());
        assertEquals(newUserName, savedUser.getFirstName());
    }

    @Test
    public void updateEmail_correctUpdate() {
        UserDTO userDTO = new UserDTO(savedUser);
        userService.updateEmail(userDTO, "testEmail@gmail.com");
        assertEquals("testEmail@gmail.com", savedSecurityUser.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmail_notPersistedUserUpdateThrowException() {
        UserDTO userDTO = new UserDTO(new User.Builder().withNames("test", "test").build());
        userService.updateEmail(userDTO, "testEmail@gmail.com");
    }

    @Test(expected = EmailFormatException.class)
    public void updateEmail_incorrectEmailUpdateThrowException() {
        UserDTO userDTO = new UserDTO(savedUser);
        userService.updateEmail(userDTO, "ail.com");
    }

    @Test(expected = EmptySecurityUserDataException.class)
    public void updatePassword_IllegalSizeThrowException() {
        UserDTO userDTO = new UserDTO(savedUser);
        userService.updatePassword(userDTO, "leth6");
    }

    @Test
    public void updatePassword_correctUpdate() {
        String oldPassword = savedSecurityUser.getPassword();
        UserDTO userDTO = new UserDTO(savedUser);
        userService.updatePassword(userDTO, "leth61");
        assertNotEquals(oldPassword, savedSecurityUser.getPassword());
    }

    @Test
    public void updateSecurityData_correctUpdate() {
        FullUserDTO fullUserDTO = new FullUserDTO(savedSecurityUser);
        fullUserDTO.setEnabled(true);
        fullUserDTO.setAuthorities(Set.of(SecurityUserAuthority.ROLE_EDIT_USER));
        userService.updateSecurityData(fullUserDTO);
        assertTrue(savedSecurityUser.isEnabled());
        assertEquals(1, savedSecurityUser.getAuthorities().size());
        assertTrue(savedSecurityUser.getAuthorities().contains(SecurityUserAuthority.ROLE_EDIT_USER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateSecurityData_changedUserLoginThrowException() {
        FullUserDTO fullUserDTO = new FullUserDTO(savedSecurityUser);
        fullUserDTO.setUsername("new_username");
        userService.updateSecurityData(fullUserDTO);
    }

    @Test
    public void checkIfUserCanBeDeleted() {
        UserDTO userDTO = new UserDTO(savedUser);
        userService.delete(userDTO);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userDAO).delete(userArgumentCaptor.capture());
        assertEquals(userDTO.getUuid(), userArgumentCaptor.getValue().getUuid().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfEmptyUserUUIDOnDeleteThrowsException() {
        userService.delete(null);
    }

    @Test
    public void checkIfGetByUUIDReturnUser() {
        User resultUser = userService.getUser(savedUser);
        Mockito.verify(userDAO).getUserByUUID(savedUser.getUuid());
        assertEquals(resultUser.getUuid(), savedUser.getUuid());
    }
}