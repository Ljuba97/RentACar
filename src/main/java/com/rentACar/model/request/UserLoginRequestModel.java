package com.rentACar.model.request;

public class UserLoginRequestModel {
    private String identification, password;

    public UserLoginRequestModel(String identification, String password) {
        this.identification = identification;
        this.password = password;
    }

    public String getIdentification() {
        return identification;
    }

    public String getPassword() {
        return password;
    }
}
