package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.SecurityUserDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.user.FullUserDTO;
import org.hetsold.bugtracker.dto.user.RegistrationDataDTO;
import org.hetsold.bugtracker.dto.user.SecurityUserDetails;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.SecurityUserAuthority;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.service.exception.TakenLoginException;
import org.hetsold.bugtracker.service.mapper.UserMapper;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hetsold.bugtracker.service.ValidationHelper.*;
import static org.passay.AllowedCharacterRule.ERROR_CODE;


@Service
public class UserServiceImpl implements UserService, UserServiceInternal, UserDetailsService {
    private UserDAO userDAO;
    private SecurityUserDAO securityUserDAO;
    private PasswordEncoder encoder;
    private String specialCharsString;

    public UserServiceImpl() {
    }

    @Autowired
    public UserServiceImpl(UserDAO userDAO, SecurityUserDAO securityUserDAO) {
        this.userDAO = userDAO;
        this.securityUserDAO = securityUserDAO;
    }

    @Autowired
    public void setEncoder(@Lazy PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public void setSpecialChars(String specialChars) {
        this.specialCharsString = specialChars;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO registerUser(FullUserDTO fullUserDTO) {
        User user = UserMapper.getUser(fullUserDTO.getUserDTO());
        SecurityUser securityUser = UserMapper.getSecurityUser(fullUserDTO);
        registerUser(user, securityUser);
        return UserMapper.getUserDTO(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO registerByData(RegistrationDataDTO registrationDataDTO) {
        SecurityUser securityUser = UserMapper.getSecurityUser(registrationDataDTO);
        User user = UserMapper.getUser(registrationDataDTO);
        shortRegistrationPreparation(securityUser, user);
        return null;
    }

    private void shortRegistrationPreparation(SecurityUser securityUser, User user) {
        validateNotNull(securityUser, "securityUser can not be null");
        /*todo: implement strategy for default user enabling*/
        securityUser.setEnabled(true);
        User fetchedUser = evaluateUser(user);
        registerUser(fetchedUser, securityUser);
    }

    private User evaluateUser(User user) {
        if (isUserDataEmpty(user)) {
            return new User(null, "user " + UUID.randomUUID().toString(), "");
        }
        return user;
    }

    private boolean isUserDataEmpty(User user) {
        return user == null || (user.getFirstName().isEmpty() && user.getLastName().isEmpty());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void registerUser(User user, SecurityUser securityUser) {
        validateSecurityUserBeforeSave(securityUser);
        if (isLoginTaken(securityUser.getUsername())) {
            throw new TakenLoginException(securityUser.getUsername());
        }
        validateUserBeforeSave(user);
        user.setUuid(UUID.randomUUID());
        userDAO.save(user);
        prepareSecurityUser(securityUser, user);
        securityUser.setUuid(UUID.randomUUID());
        securityUserDAO.save(securityUser);
    }

    private void prepareSecurityUser(SecurityUser securityUser, User user) {
        securityUser.setUser(user);
        securityUser.setPassword(encodePassword(securityUser.getPassword()));
        simplifySecurityUserLockData(securityUser);
    }

    private String encodePassword(String password) {
        if (encoder != null) {
            return encoder.encode(password);
        }
        return password;
    }

    private void simplifySecurityUserLockData(SecurityUser securityUser) {
        securityUser.setAccountNonExpired(securityUser.isEnabled());
        securityUser.setAccountNonLocked(securityUser.isEnabled());
        securityUser.setCredentialsNonExpired(securityUser.isEnabled());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(UserDTO userDTO) {
        updateUser(UserMapper.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User updateUser(User updatedUser) {
        validateNotNullEntityAndUUID(updatedUser);
        validateUserLastName(updatedUser);
        User oldUser = getUser(updatedUser);
        validateNotNull(oldUser, USER_NOT_PERSISTED);
        oldUser.update(updatedUser);
        return oldUser;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUser(User user) {
        validateNotNullEntityAndUUID(user);
        return userDAO.getUserByUUID(user.getUuid());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(User user) {
        validateNotNullEntityAndUUID(user);
        User fetchedUser = getUser(user);
        userDAO.delete(fetchedUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SecurityUser getSecurityUser(SecurityUser securityUser) {
        validateNotNullEntityAndUUID(securityUser);
        return securityUserDAO.getSecurityUserByUuid(securityUser.getUuid());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SecurityUser getSecurityUserByUser(User user) {
        User fetchedUser = getUser(user);
        validateNotNull(fetchedUser, USER_NOT_PERSISTED);
        return securityUserDAO.getSecurityUserByUserUuid(fetchedUser.getUuid());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSecurityUser(SecurityUser securityUser) {
        SecurityUser fetchedSecurityUser = getSecurityUser(securityUser);
        securityUserDAO.delete(fetchedSecurityUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void fullUserUpdate(FullUserDTO fullUserDTO) {
        User user = UserMapper.getUser(fullUserDTO.getUserDTO());
        SecurityUser securityUser = UserMapper.getSecurityUser(fullUserDTO);
        fullUserUpdate(user, securityUser);
    }

    private void fullUserUpdate(User user, SecurityUser securityUser) {
        updateUser(user);
        updateSecurityUser(securityUser);
    }

    private void updateSecurityUser(SecurityUser securityUser) {
        validateNotNullEntityAndUUID(securityUser);
        validateEmail(securityUser.getEmail());
        SecurityUser oldSecurityUser = getSecurityUser(securityUser);
        if (isLoginChangedOnTaken(securityUser, oldSecurityUser)) {
            throw new TakenLoginException(securityUser.getUsername());
        }
        if (securityUser.getPassword() != null && !securityUser.getPassword().isEmpty()) {
            updateAndEncodeSecurityUserPassword(securityUser, securityUser.getPassword());
            oldSecurityUser.fullUpdate(securityUser);
        } else {
            oldSecurityUser.updateNotSensitiveData(securityUser);
        }
    }

    private boolean isLoginChangedOnTaken(SecurityUser securityUser, SecurityUser oldState) {
        return !oldState.getUsername().equals(securityUser.getUsername()) && isLoginTaken(securityUser.getUsername());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateEmail(UserDTO userDTO, String email) {
        updateEmail(UserMapper.getUser(userDTO), email);
    }

    private void updateEmail(User user, String email) {
        validateNotNullEntityAndUUID(user);
        validateEmail(email);
        SecurityUser securityUser = getSecurityUserByUser(user);
        validateNotNull(user, USER_NOT_PERSISTED);
        securityUser.setEmail(email);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(UserDTO userDTO, String newPassword) {
        updatePassword(UserMapper.getUser(userDTO), newPassword);
    }

    private void updatePassword(User user, String newPassword) {
        validateNotNullEntityAndUUID(user);
        SecurityUser securityUser = getSecurityUserByUser(user);
        validateNotNull(securityUser, "securityUser not linked for this user");
        updateAndEncodeSecurityUserPassword(securityUser, newPassword);
    }

    private void updateAndEncodeSecurityUserPassword(SecurityUser securityUser, String newPassword) {
        validatePassword(newPassword);
        securityUser.setPassword(encodePassword(newPassword));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSecurityData(FullUserDTO fullUserDTO) {
        updateSecurityData(UserMapper.getSecurityUser(fullUserDTO));
    }

    private void updateSecurityData(SecurityUser securityUser) {
        validateNotNullEntityAndUUID(securityUser);
        SecurityUser oldSecurityUser = getSecurityUser(securityUser);
        if (!oldSecurityUser.getUsername().equals(securityUser.getUsername())) {
            throw new IllegalArgumentException("user login can not be changed");
        }
        validateNotNull(oldSecurityUser, "securityUser is not persisted");
        oldSecurityUser.updateNotSensitiveData(securityUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeUserRoles(UserDTO userDTO, Set<SecurityUserAuthority> securityUserAuthorities) {
        changeUserRoles(UserMapper.getUser(userDTO), securityUserAuthorities);
    }

    private void changeUserRoles(User user, Set<SecurityUserAuthority> securityUserAuthorities) {
        validateNotNullEntityAndUUID(user);
        SecurityUser securityUser = securityUserDAO.getSecurityUserByUserUuid(user.getUuid());
        securityUser.setAuthorities(securityUserAuthorities);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean isLoginTaken(String login) {
        return securityUserDAO.isLoginTaken(login);
    }

    @Override
    public String getAutogeneratedPassword() {
        return generatePassString();
    }

    private String generatePassString() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 2);
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 2);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 2);
        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                if (specialCharsString.isEmpty()) {
                    return "!@#$%^&*()_+";
                } else {
                    return specialCharsString;
                }
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars, 2);
        return gen.generatePassword(6, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<UserDTO> getUsers(Contract contract, int first, int limit) {
        return UserMapper.getUserDTOS(userDAO.getUsers(contract, first, limit));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getUsersCount(Contract contract) {
        return userDAO.getUsersCount(contract);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(UserDTO userDTO) {
        deleteUser(UserMapper.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserDTO getUser(UserDTO userDTO) {
        return UserMapper.getUserDTO(getUser(UserMapper.getUser(userDTO)));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FullUserDTO getFullUserByUser(UserDTO userDTO) {
        User user = UserMapper.getUser(userDTO);
        user = getUser(user);
        validateNotNull(user, "user not exists");
        return UserMapper.getFullUserDTO(securityUserDAO.getSecurityUserByUserUuid(user.getUuid()));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SecurityUserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        SecurityUser securityUser = securityUserDAO.getSecUserByUsername(login);
        if (securityUser != null) {
            return UserMapper.getSecurityUserDetails(securityUser);
        } else {
            throw new UsernameNotFoundException("login error");
        }
    }
}