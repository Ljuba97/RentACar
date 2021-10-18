package com.rentACar.controller;

import com.rentACar.dao.UserDao;
import com.rentACar.dao.UserDaoSQL;
import com.rentACar.model.UserModel;
import com.rentACar.model.request.AdminUpdateUserRequestModel;
import com.rentACar.model.request.UserLoginRequestModel;
import com.rentACar.model.request.UserRegisterRequestModel;
import com.rentACar.model.request.UserUpdateRequestModel;
import com.rentACar.model.response.*;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private static final UserDao ud = new UserDaoSQL();

    private static boolean isEmailValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private static String getSHA256(String password) {
        String hashValue = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] digestedBytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digestedBytes){
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @PatchMapping("/admin/update/{id}")
    public AdminUpdateUserResponseModel updateUser(@RequestBody AdminUpdateUserRequestModel user, @RequestHeader(value = "authorization") UUID adminId, @PathVariable("id") UUID userId) {
        String username = "", email = "";
        boolean isUser = false, isAdmin = false, usernameIsValid = true, emailIsValid = true;

        for (var x : ud.getAllAdminId()) {
            if (x.equals(adminId)) {
                isAdmin = true;
                break;
            }
        }

        for (var x : ud.allUsers()) {
            if (x.getUserId().equals(userId))
                isUser = true;
            if (x.getUsername().equals(user.getUsername()))
                usernameIsValid = false;
            if (x.getEmail().equals(user.getEmail()))
                emailIsValid = false;
        }

        if (isAdmin) {
            if (isUser) {
                if (user.getUsername().length() >= 3 && usernameIsValid)
                    username = user.getUsername();
                if (isEmailValid(user.getEmail()) && emailIsValid)
                    email = user.getEmail();
                ud.updateUser(new AdminUpdateUserRequestModel(username, email, user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getPersonalNumber(), user.getImage()), userId);
                return new AdminUpdateUserResponseModel(true, "Changed ...");
            }

            return new AdminUpdateUserResponseModel(false, "User with this id doesn't exist!");
        }

        return new AdminUpdateUserResponseModel(false, "Authorization problem!");
    }

    @PostMapping("/users/register")
    public UserRegisterResponseModel registerUser(@RequestBody UserRegisterRequestModel user) {
        for (var x : ud.allUsers()) {
            if (x.getEmail().equals(user.getEmail()))
                return new UserRegisterResponseModel(false, "Account with this email already exists!");
            if (x.getUsername().equals(user.getUsername()))
                return new UserRegisterResponseModel(false, "Account with this username already exists!");
        }

        if (!isEmailValid(user.getEmail()))
            return new UserRegisterResponseModel(false, "The format of this email is invalid!");
        if (user.getUsername().length() < 3)
            return new UserRegisterResponseModel(false, "Username must be at least 3 characters long!");
        if (user.getPassword().length() < 8 || !user.getPassword().matches(".*\\d.*") || !user.getPassword().matches(".*[a-zA-Z].*"))
            return new UserRegisterResponseModel(false, "Password must be at least 8 characters long, contain 1 number and at least 1 letter!");

        ud.registerUser(new UserModel(user.getUsername(), user.getEmail(), getSHA256(user.getPassword())));
        return new UserRegisterResponseModel(true, user.getUsername() + " - " + user.getEmail() + " has registered successfully!");
    }

    @PostMapping("/users/login")
    public UserLoginResponseModel loginUser(@RequestBody UserLoginRequestModel user) {
        for (var x : ud.allUsers()) {
            if ((x.getUsername().equals(user.getIdentification()) || x.getEmail().equals(user.getIdentification())) && x.getPassword().equals(getSHA256(user.getPassword())))
                return new UserLoginResponseModel(true, x.getUserId().toString());
        }

        return new UserLoginResponseModel(false, "Invalid username/email or password!");
    }

    @PatchMapping("/users/{id}")
    public UserUpdateResponseModel updateUser(@RequestBody UserUpdateRequestModel user, @PathVariable("id") UUID userId) {
        UserModel um = ud.getUser(userId);
        boolean usernameIsValid = true;
        String username = "";
        String newPassword = "";

        for (var x : ud.allUsers()) {
            if (x.getUsername().equals(user.getUsername())) {
                usernameIsValid = false;
                break;
            }
        }

        if (!um.getPassword().equals(getSHA256(user.getPassword())))
            return new UserUpdateResponseModel(false, "Password not valid!");
        if (user.getUsername().length() >= 3 && usernameIsValid)
            username = user.getUsername();
        if (user.getNewPassword().length() >= 8 && user.getNewPassword().matches(".*\\d.*") && user.getNewPassword().matches(".*[a-zA-Z].*"))
            newPassword = user.getNewPassword();

        if (!newPassword.isEmpty())
            ud.updateUser(new UserUpdateRequestModel(username, user.getPassword(), getSHA256(newPassword), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getImage()), userId);
        else
            ud.updateUser(new UserUpdateRequestModel(username, user.getPassword(), newPassword, user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getImage()), userId);
        return new UserUpdateResponseModel(true, "Changed ...");
    }

    @GetMapping("users/{id}")
    public UserResponseModel getUser(@PathVariable("id") UUID userId) {
        UserModel um = ud.getUser(userId);
        return new UserResponseModel(um.getUsername(), um.getEmail(), um.getFirstName(), um.getLastName(), um.getPhoneNumber(), um.getPersonalNumber(), um.getImage());
    }

    @GetMapping("users")
    public List<UserResponseModel> allUsers(@RequestHeader(value = "authorization") UUID adminId) {
        List<UserResponseModel> list = new ArrayList<>();
        boolean admin = false;
        for (var x : ud.getAllAdminId()) {
            if (x.equals(adminId)) {
                admin = true;
                break;
            }
        }

        if (admin) {
            for (var x : ud.allUsers())
                list.add(new UserResponseModel(x.getUsername(), x.getEmail(), x.getFirstName(), x.getLastName(), x.getPhoneNumber(), x.getPersonalNumber(), x.getImage()));
        }
        return list;
    }
}
