package com.online.library.app.controller;

import com.online.library.app.dto.request.UserRequest;
import com.online.library.app.dto.response.UserResponse;
import com.online.library.app.exceptionhandler.ErrorResponse;
import com.online.library.app.exceptionhandler.login.UserWithEmailNotFound;
import com.online.library.app.exceptionhandler.login.UserWithUsernameNotFound;
import com.online.library.app.exceptionhandler.login.WrongPasswordException;
import com.online.library.app.exceptionhandler.user.UserDeletionException;
import com.online.library.app.exceptionhandler.user.UserUpdateException;
import com.online.library.app.exceptionhandler.user.UserWithEmailAlreadyExistsException;
import com.online.library.app.service.interfaces.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServiceInterface userService;

    @PostMapping("/api/create-user")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/login")
    public ResponseEntity<UserResponse> login(
            @RequestParam String username,
            @RequestParam String password) {
        return ResponseEntity.ok(userService.login(username, password));
    }

    @DeleteMapping("/api/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/api/edit-user/{id}")
    public ResponseEntity<Void> editUser(@RequestBody UserRequest request, @PathVariable Long id) {
        userService.editUserInfo(request, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(UserWithEmailAlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleUserWithEmailAlreadyExistsException(
            UserWithEmailAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(UserWithEmailNotFound.class)
    public final ResponseEntity<ErrorResponse> handleUserWithEmailNotFound(UserWithEmailNotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(UserWithUsernameNotFound.class)
    public final ResponseEntity<ErrorResponse> handleUserWithUsernameNotFound(UserWithUsernameNotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(WrongPasswordException.class)
    public final ResponseEntity<ErrorResponse> handleWrongPasswordException(WrongPasswordException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), e.getMessage()));
    }

    @ExceptionHandler(UserUpdateException.class)
    public final ResponseEntity<ErrorResponse> handleUserUpdateException(UserUpdateException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(UserDeletionException.class)
    public final ResponseEntity<ErrorResponse> handleUserDeletionException(UserDeletionException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }
}
