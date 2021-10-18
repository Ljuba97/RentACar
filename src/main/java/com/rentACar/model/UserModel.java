package com.rentACar.model;

import java.util.UUID;

public class UserModel {
    private UUID userId;
    private String username, email, password, firstName, lastName, phoneNumber, personalNumber, image;
    private boolean admin;

    public UserModel(UUID userId, String username, String email, String password, String firstName, String lastName, String phoneNumber, String personalNumber, String image, boolean admin) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.personalNumber = personalNumber;
        this.image = image;
        this.admin = admin;
    }

    public UserModel(String username, String email, String password) {
        this.userId = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.admin = false;
    }

    public UUID getUserId() {
        return userId;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getImage() {
        return image;
    }

    public boolean isAdmin() {
        return admin;
    }
}
