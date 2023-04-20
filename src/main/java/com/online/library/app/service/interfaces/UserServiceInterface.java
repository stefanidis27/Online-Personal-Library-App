package com.online.library.app.service.interfaces;

import com.online.library.app.dto.request.UserRequest;
import com.online.library.app.dto.response.UserResponse;

import java.util.List;

public interface UserServiceInterface {
    void register(UserRequest request);

    UserResponse login(String username, String password);

    void deleteUser(Long id);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    void editUserInfo(UserRequest request, Long id);
}
