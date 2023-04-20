package com.online.library.app.exceptionhandler.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserWithEmailAlreadyExistsException extends RuntimeException {

    private String message;
}
