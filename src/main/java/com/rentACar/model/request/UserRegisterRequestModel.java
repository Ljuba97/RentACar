package com.rentACar.model.request;

public class UserRegisterRequestModel {
    private String username, email, password;

    public UserRegisterRequestModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
