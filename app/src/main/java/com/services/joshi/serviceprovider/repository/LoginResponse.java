package com.services.joshi.serviceprovider.repository;

import com.services.joshi.serviceprovider.models.User;

import java.util.List;

public class LoginResponse {

    private boolean error;
    private String message;
    private List<User> user;


    public LoginResponse(boolean error, String message, List<User> user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<User> getUser() {
        return user;
    }
}
