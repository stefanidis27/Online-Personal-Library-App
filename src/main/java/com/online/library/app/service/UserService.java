package com.online.library.app.service;

import com.online.library.app.dto.request.UserRequest;
import com.online.library.app.dto.response.UserResponse;
import com.online.library.app.exceptionhandler.login.UserWithEmailNotFound;
import com.online.library.app.exceptionhandler.login.UserWithUsernameNotFound;
import com.online.library.app.exceptionhandler.login.WrongPasswordException;
import com.online.library.app.exceptionhandler.user.UserDeletionException;
import com.online.library.app.exceptionhandler.user.UserNotFoundException;
import com.online.library.app.exceptionhandler.user.UserUpdateException;
import com.online.library.app.exceptionhandler.user.UserWithEmailAlreadyExistsException;
import com.online.library.app.model.User;
import com.online.library.app.repository.UserRepository;
import com.online.library.app.service.interfaces.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    @Override
    public void register(UserRequest request) {
        User user = new User();
        user.setCollections(new ArrayList<>());
        user.setUsername(request.getUsername());
        user.setPassword(encryptPassword(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setType(request.getType());
        user.setNumberOfCreatedCollections(0);

        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new UserWithEmailAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }
    }

    private String encryptPassword(String password) {
        String encryptedPassword = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            encryptedPassword = s.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return encryptedPassword;
    }

    @Override
    public UserResponse login(String username, String password) {
        User user = null;
        if (username.contains("@")) {
            user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserWithEmailNotFound("User with this email does not exist."));
        } else {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserWithUsernameNotFound("User with this username does not exist."));
        }

        if (!user.getPassword().equals(encryptPassword(password))) {
            throw new WrongPasswordException("Wrong password.");
        }

        return new UserResponse(
                user.getId(),
                user.getType(),
                user.getUsername(),
                user.getNumberOfCreatedCollections());
    }

    @Override
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new UserDeletionException("User with this id could not be deleted.");
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserResponse(
                user.getId(),
                user.getType(),
                user.getUsername(),
                user.getNumberOfCreatedCollections()
        )).toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with this id was not found."));

        return new UserResponse(
                user.getId(),
                user.getType(),
                user.getUsername(),
                user.getNumberOfCreatedCollections());
    }

    @Override
    public void editUserInfo(UserRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with this id was not found."));
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            user.setPassword(encryptPassword(request.getPassword()));
        }
        if (request.getType() != null) {
            user.setType(request.getType());
        }

        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new UserUpdateException("Failed to update user with this id.");
        }
    }
}
