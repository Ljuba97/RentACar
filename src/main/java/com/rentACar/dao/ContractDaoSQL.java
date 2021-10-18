package com.rentACar.dao;

import com.rentACar.model.ContractModel;
import com.rentACar.model.request.ContractPendingRequestModel;
import org.apache.tomcat.jni.Local;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ContractDaoSQL implements ContractDao{

    @Override
    public double getCarPrice(UUID carId, LocalDate startDate, LocalDate endDate) {
        try {
            PreparedStatement st = conn.prepareStatement("SELECT price * (?::date - ?::date) FROM cars WHERE car_id = ?");

            st.setDate(1, Date.valueOf(endDate));
            st.setDate(2, Date.valueOf(startDate));
            st.setString(3, carId.toString());

            ResultSet rs = st.executeQuery();
            if (rs.next())
                return rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void addContract(ContractPendingRequestModel contract) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO contracts VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            st.setString(1, contract.getContractId().toString());
            st.setString(2, contract.getUserId().toString());
            st.setString(3, contract.getCarId().toString());
            st.setDate(4, Date.valueOf(contract.getStartDate()));
            st.setDate(5, Date.valueOf(contract.getEndDate()));
            st.setDouble(6, contract.getTotalPrice());
            st.setBoolean(7, contract.isSigned());
            st.setBoolean(8, contract.isApproved());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void approveContract(UUID contractId) {
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE contracts SET approved = true WHERE contract_id = ?");

            st.setString(1, contractId.toString());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteContract(UUID contractId) {
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM contracts WHERE contract_id = ?");

            st.setString(1, contractId.toString());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LocalDate> calendar(UUID carId) {
        List<LocalDate> list = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT generate_series(start_date, end_date, interval '1 day')::date AS dates " +
                    "FROM contracts WHERE car_id = '" + carId.toString() + "' ORDER BY dates");

            while (rs.next())
                list.add(rs.getDate(1).toLocalDate());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<LocalDate> betweenDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> list = new ArrayList<>();

        try {
            PreparedStatement st = conn.prepareStatement("SELECT generate_series(?, ?, interval '1 day')::date AS dates");

            st.setDate(1, Date.valueOf(startDate));
            st.setDate(2, Date.valueOf(endDate));

            ResultSet rs = st.executeQuery();

            while (rs.next())
                list.add(rs.getDate(1).toLocalDate());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public HashMap<String, List<LocalDate>> allBetweenDates() {
        HashMap<String, List<LocalDate>> map = new HashMap<>();
        List<LocalDate> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT generate_series(start_date, end_date, interval '1 day')::date AS dates, car_id FROM contracts");

            while (rs.next()) {
                if (map.containsKey(rs.getString(2)))
                    list = map.get(rs.getString(2));

                list.add(rs.getDate(1).toLocalDate());
                map.put(rs.getString(2), list);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public List<ContractModel> allUserContracts(UUID userId) {
        List<ContractModel> list = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM contracts WHERE user_id = '" + userId.toString() + "'");

            while (rs.next())
                list.add(new ContractModel(UUID.fromString(rs.getString(1)),UUID.fromString(rs.getString(2)),
                        UUID.fromString(rs.getString(3)), rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate(),
                        rs.getDouble(6), rs.getBoolean(7), rs.getBoolean(8)));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<ContractModel> allContracts() {
        List<ContractModel> list = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM contracts");

            while (rs.next())
                list.add(new ContractModel(UUID.fromString(rs.getString(1)),UUID.fromString(rs.getString(2)),
                        UUID.fromString(rs.getString(3)), rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate(),
                        rs.getDouble(6), rs.getBoolean(7), rs.getBoolean(8)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
