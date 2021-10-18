package com.rentACar.dao;

import com.rentACar.db.DatabaseConnection;
import com.rentACar.model.UserModel;
import com.rentACar.model.request.AdminUpdateUserRequestModel;
import com.rentACar.model.request.UserUpdateRequestModel;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public interface UserDao {
    void registerUser(UserModel user);
    void updateUser(UserUpdateRequestModel user, UUID userId);
    void updateUser(AdminUpdateUserRequestModel user, UUID userId);
    UserModel getUser(UUID userId);
    List<UUID> getAllAdminId();
    List<UserModel> allUsers();
    Connection conn = DatabaseConnection.getConnection();
}
