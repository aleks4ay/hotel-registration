package com.aleks4ay.hotel.registration.exception;

public class UserCreateException extends RuntimeException{

    public UserCreateException(int status) {
        super("User creation failed: " + status);
    }
}
