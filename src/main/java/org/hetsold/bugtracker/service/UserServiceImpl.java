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
import org.hetsold.bugtracker.service.exception.LoginRegisteredException;
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
    public UserDTO register(RegistrationDataDTO registrationDataDTO) {
        SecurityUser securityUser = UserMapper.getSecurityUser(registrationDataDTO);
        User user = UserMapper.getUser(registrationDataDTO);
        shortRegistrationPreparation(securityUser, user);
        return null;
    }

    private void shortRegistrationPreparation(SecurityUser securityUser, User user) {
        validateNotNull(securityUser, "securityUser can not be null");
        /*todo: implement strategy*/
        securityUser.setEnabled(true);
        if (isRegisteredUserNullOrDataNotFiled(user)) {
            user = new User(null, "user " + UUID.randomUUID().toString(), "");
        }
        registerUser(user, securityUser);
    }

    private boolean isRegisteredUserNullOrDataNotFiled(User user) {
        if (user == null || (user.getFirstName().isEmpty() && user.getLastName().isEmpty())) {
            return true;
        }
        return false;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO register(FullUserDTO fullUserDTO) {
        User user = UserMapper.getUser(fullUserDTO.getUserDTO());
        SecurityUser securityUser = UserMapper.getSecurityUser(fullUserDTO);
        registerUser(user, securityUser);
        return UserMapper.getUserDTO(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void registerUser(User user, SecurityUser securityUser) {
        validateUserBeforeSave(user);
        validateSecurityUserBeforeSave(securityUser);
        if (isLoginTaken(securityUser.getUsername())) {
            throw new LoginRegisteredException(securityUser.getUsername());
        }
        user.setUuid(UUID.randomUUID());
        userDAO.save(user);
        prepareSecurityUser(securityUser, user);
        securityUser.setUuid(UUID.randomUUID());
        securityUserDAO.save(securityUser);
    }

    private void prepareSecurityUser(SecurityUser securityUser, User user) {
        securityUser.setUser(user);
        encodePassword(securityUser);
        simplifySecurityUserLockData(securityUser);
    }

    private void encodePassword(SecurityUser securityUser) {
        if (encoder != null) {
            securityUser.setPassword(encoder.encode(securityUser.getPassword()));
        }
    }

    private void simplifySecurityUserLockData(SecurityUser securityUser) {
        securityUser.setAccountNonExpired(securityUser.isEnabled());
        securityUser.setAccountNonLocked(securityUser.isEnabled());
        securityUser.setCredentialsNonExpired(securityUser.isEnabled());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User updateUser(User updatedUser) {
        validateUserAndUUID(updatedUser);
        validateUserLastName(updatedUser);
        User oldUser = getUser(updatedUser);
        validateNotNull(oldUser, "user is not persisted");
        oldUser.update(updatedUser);
        return oldUser;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUser(User user) {
        validateUserAndUUID(user);
        return userDAO.getUserByUUID(user.getUuid());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(User user) {
        validateUserAndUUID(user);
        user = getUser(user);
        userDAO.delete(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SecurityUser getSecurityUser(SecurityUser securityUser) {
        validateSecurityUserAndUUID(securityUser);
        return securityUserDAO.getSecurityUserByUuid(securityUser.getUuid());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SecurityUser getSecurityUserByUser(User user) {
        user = getUser(user);
        validateNotNull(user, "user is not persisted");
        return securityUserDAO.getSecurityUserByUserUuid(user.getUuid());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSecurityUser(SecurityUser securityUser) {
        securityUser = getSecurityUser(securityUser);
        securityUserDAO.delete(securityUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserProfileData(UserDTO userDTO) {
        updateUser(UserMapper.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateEmail(UserDTO userDTO, String email) {
        updateEmail(UserMapper.getUser(userDTO), email);
    }

    public void updateEmail(User user, String email) {
        validateUserAndUUID(user);
        validateEmail(email);
        SecurityUser securityUser = getSecurityUserByUser(user);
        validateNotNull(user, "user is not persisted");
        securityUser.setEmail(email);
    }

    public void updatePassword(User user, String newPassword) {
        validateUserAndUUID(user);
        updatePassword(getSecurityUserByUser(user), newPassword);
    }

    private void updatePassword(SecurityUser securityUser, String newPassword) {
        validatePassword(newPassword);
        securityUser.setPassword(newPassword);
        encodePassword(securityUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(UserDTO userDTO, String newPassword) {
        updatePassword(UserMapper.getUser(userDTO), newPassword);
    }

    private void updateUser(User user, SecurityUser securityUser) {
        updateUser(user);
        updateSecurityData(securityUser);
        if (securityUser.getPassword() != null && !securityUser.getPassword().isEmpty()) {
            String password = securityUser.getPassword();
            securityUser = getSecurityUser(securityUser);
            updatePassword(securityUser, password);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(FullUserDTO fullUserDTO) {
        User user = UserMapper.getUser(fullUserDTO.getUserDTO());
        SecurityUser securityUser = UserMapper.getSecurityUser(fullUserDTO);
        updateUser(user, securityUser);
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

    /*todo: change*/
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSecurityData(FullUserDTO fullUserDTO) {
        updateSecurityData(UserMapper.getSecurityUser(fullUserDTO));
    }

    private void updateSecurityData(SecurityUser newSecurityUser) {
        validateSecurityUserAndUUID(newSecurityUser);
        SecurityUser oldSecurityUser = getSecurityUser(newSecurityUser);
        if (!oldSecurityUser.getUsername().equals(newSecurityUser.getUsername())) {
            throw new IllegalArgumentException("user login can not be changed");
        }
        validateNotNull(oldSecurityUser, "securityUser is not persisted");
        oldSecurityUser.update(newSecurityUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeUserRoles(UserDTO userDTO, Set<SecurityUserAuthority> securityUserAuthorities) {
        changeUserRoles(UserMapper.getUser(userDTO), securityUserAuthorities);
    }

    private void changeUserRoles(User user, Set<SecurityUserAuthority> securityUserAuthorities) {
        validateUserAndUUID(user);
        SecurityUser securityUser = securityUserDAO.getSecurityUserByUserUuid(user.getUuid());
        securityUser.setAuthorities(securityUserAuthorities);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean isLoginTaken(String login) {
        return securityUserDAO.isLoginTaken(login);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<UserDTO> getUsers(Contract contract, int first, int limit, boolean dateAsc) {
        return UserMapper.getUserDTOS(userDAO.getUsers(contract, first, limit, dateAsc));
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