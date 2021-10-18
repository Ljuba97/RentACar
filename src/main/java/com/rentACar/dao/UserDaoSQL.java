package com.rentACar.dao;

import com.rentACar.model.UserModel;
import com.rentACar.model.request.AdminUpdateUserRequestModel;
import com.rentACar.model.request.UserUpdateRequestModel;

import javax.swing.plaf.nimbus.State;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDaoSQL implements UserDao{
    @Override
    public void registerUser(UserModel user) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO users (user_id, username, email, password, admin) VALUES (?, ?, ?, ?, ?)");

            st.setString(1, user.getUserId().toString());
            st.setString(2, user.getUsername());
            st.setString(3, user.getEmail());
            st.setString(4, user.getPassword());
            st.setBoolean(5, user.isAdmin());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(UserUpdateRequestModel user, UUID userId) {
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE users SET username = ?, password = ?, " +
                    "first_name = ?, last_name = ?, phone_number = ?, image = ? WHERE user_id = ?");

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT username, password, first_name, last_name, phone_number, " +
                    "image FROM users WHERE user_id = '" + userId.toString() + "'");

            if (rs.next()) {
                if (user.getUsername().isEmpty())
                    st.setString(1, rs.getString(1));
                else
                    st.setString(1, user.getUsername());
                if (user.getNewPassword().isEmpty())
                    st.setString(2, rs.getString(2));
                else
                    st.setString(2, user.getNewPassword());
                if (user.getFirstName().isEmpty())
                    st.setString(3, rs.getString(3));
                else
                    st.setString(3, user.getFirstName());
                if (user.getLastName().isEmpty())
                    st.setString(4, rs.getString(4));
                else
                    st.setString(4, user.getLastName());
                if (user.getPhoneNumber().isEmpty())
                    st.setString(5, rs.getString(5));
                else
                    st.setString(5, user.getPhoneNumber());
                if (user.getImage().isEmpty())
                    st.setString(6, rs.getString(6));
                else
                    st.setString(6, user.getImage());
                st.setString(7, userId.toString());
            }
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(AdminUpdateUserRequestModel user, UUID userId) {
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE users SET username = ?, email = ?, first_name = ?, " +
                    "last_name = ?, phone_number = ?, personal_number = ?, image = ? WHERE user_id = ?");

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT username, email, first_name, last_name, phone_number, " +
                    "personal_number, image FROM users WHERE user_id = '" + userId.toString() + "'");

            if (rs.next()) {
                if (user.getUsername().isEmpty())
                    st.setString(1, rs.getString(1));
                else
                    st.setString(1, user.getUsername());
                if (user.getEmail().isEmpty())
                    st.setString(2, rs.getString(2));
                else
                    st.setString(2, user.getEmail());
                if (user.getFirstName().isEmpty())
                    st.setString(3, rs.getString(3));
                else
                    st.setString(3, user.getFirstName());
                if (user.getLastName().isEmpty())
                    st.setString(4, rs.getString(4));
                else
                    st.setString(4, user.getLastName());
                if (user.getPhoneNumber().isEmpty())
                    st.setString(5, rs.getString(5));
                else
                    st.setString(5, user.getPhoneNumber());
                if (user.getPersonalNumber().isEmpty())
                    st.setString(6, rs.getString(6));
                else
                    st.setString(6, user.getPersonalNumber());
                if (user.getImage().isEmpty())
                    st.setString(7, rs.getString(7));
                else
                    st.setString(7, user.getImage());
                st.setString(8, userId.toString());
            }

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserModel getUser(UUID userId) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE user_id = '" + userId + "'");

            if (rs.next())
                return new UserModel(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                        rs.getString(8), rs.getString(9), rs.getBoolean(10));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<UUID> getAllAdminId() {
        List<UUID> list = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT user_id FROM users WHERE admin IS TRUE");

            while (rs.next())
                list.add(UUID.fromString(rs.getString(1)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<UserModel> allUsers() {
        List<UserModel> list = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users");

            while (rs.next())
                list.add(new UserModel(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                        rs.getString(8), rs.getString(9), rs.getBoolean(10)));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
