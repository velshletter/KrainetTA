package com.velshletter.auth_service.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String msg) {
        super(msg);
    }

}
