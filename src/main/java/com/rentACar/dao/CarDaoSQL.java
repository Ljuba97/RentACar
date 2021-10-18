package com.rentACar.dao;

import com.rentACar.model.CarModel;
import com.rentACar.model.request.CarAddRequestModel;
import com.rentACar.model.request.CarUpdateRequestModel;
import com.rentACar.model.response.CarResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class CarDaoSQL implements CarDao{
    @Override
    public CarResponseModel getCarById(UUID carId) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM cars WHERE car_id = '" + carId.toString() + "'");

            if (rs.next())
                return new CarResponseModel(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getString(7),
                        rs.getDouble(8), rs.getInt(9), rs.getString(10), rs.getInt(11),
                        rs.getBoolean(12), rs.getString(13), rs.getString(14));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CarResponseModel> allCars() {
        List<CarResponseModel> list = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM cars");

            while (rs.next())
                list.add(new CarResponseModel(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getString(7),
                        rs.getDouble(8), rs.getInt(9), rs.getString(10), rs.getInt(11),
                        rs.getBoolean(12), rs.getString(13), rs.getString(14)));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<CarResponseModel> searchAvailableCars(LocalDate startDate, LocalDate endDate, String make, String model, int year, boolean automatic, double price, int power, int doors, HashSet<UUID> unavailableCarId) {
        List<CarResponseModel> list = new ArrayList<>();
        String str = "";
        if (!unavailableCarId.isEmpty()) {
            for (var x : unavailableCarId)
                str = str.concat("'").concat(x.toString()).concat("', ");
            str = str.substring(0, str.length() - 2);
        }

        System.out.println(str);

        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM cars WHERE " +
                    "(CASE WHEN ? != '' THEN LOWER(make) LIKE ? ELSE make != '' END) AND " +
                    "(CASE WHEN ? != '' THEN LOWER(model) LIKE ? ELSE model != '' END) AND " +
                    "year >= ? AND automatic = ? AND price <= ? AND power <= ? AND doors = ? AND car_id NOT IN (" + str + ")");

            st.setString(1, make);
            st.setString(2, "%" + make.toLowerCase() + "%");
            st.setString(3, model);
            st.setString(4, "%" + model.toLowerCase() + "%");
            st.setInt(5, year);
            st.setBoolean(6, automatic);
            st.setDouble(7, price);
            st.setInt(8, power);
            st.setInt(9, doors);

            ResultSet rs = st.executeQuery();
            while (rs.next())
                list.add(new CarResponseModel(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getString(7),
                        rs.getDouble(8), rs.getInt(9), rs.getString(10), rs.getInt(11),
                        rs.getBoolean(12), rs.getString(13), rs.getString(14)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<CarResponseModel> searchCars(String make, String model, int year, boolean automatic, double price, int power, int doors) {
        List<CarResponseModel> list = new ArrayList<>();

        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM cars WHERE " +
                    "(CASE WHEN ? != '' THEN LOWER(make) LIKE ? ELSE make != '' END) AND " +
                    "(CASE WHEN ? != '' THEN LOWER(model) LIKE ? ELSE model != '' END) AND " +
                    "year >= ? AND automatic = ? AND price <= ? AND power <= ? AND doors = ?");

            st.setString(1, make);
            st.setString(2, "%" + make.toLowerCase() + "%");
            st.setString(3, model);
            st.setString(4, "%" + model.toLowerCase() + "%");
            st.setInt(5, year);
            st.setBoolean(6, automatic);
            st.setDouble(7, price);
            st.setInt(8, power);
            st.setInt(9, doors);

            ResultSet rs = st.executeQuery();
            while (rs.next())
                list.add(new CarResponseModel(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getString(7),
                        rs.getDouble(8), rs.getInt(9), rs.getString(10), rs.getInt(11),
                        rs.getBoolean(12), rs.getString(13), rs.getString(14)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void addCar(CarAddRequestModel car) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO cars VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            st.setString(1, car.getCarId().toString());
            st.setString(2, car.getLicencePlate());
            st.setString(3, car.getMake());
            st.setString(4, car.getModel());
            st.setInt(5, car.getYear());
            st.setInt(6, car.getEngineCapacity());
            st.setString(7, car.getColor());
            st.setDouble(8, car.getPrice());
            st.setInt(9, car.getDoors());
            st.setString(10, car.getSize());
            st.setInt(11, car.getPower());
            st.setBoolean(12, car.isAutomatic());
            st.setString(13, car.getFuel());
            st.setString(14, car.getImage());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCar(CarUpdateRequestModel car, UUID carId) {
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE cars SET licence_plate = ?, make = ?, model = ?, " +
                    "year = ?, engine_capacity = ?, color = ?, price = ?, doors = ?, size = ?, power = ?, " +
                    "automatic = ?, fuel = ?, image = ? WHERE car_id = ?");

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM cars WHERE car_id = '" + carId.toString() + "'");

            if (rs.next()) {

                if (car.getLicencePlate().isEmpty())
                    st.setString(1, rs.getString(2));
                else
                    st.setString(1, car.getLicencePlate());
                if (car.getMake().isEmpty())
                    st.setString(2, rs.getString(3));
                else
                    st.setString(2, car.getMake());
                if (car.getModel().isEmpty())
                    st.setString(3, rs.getString(4));
                else
                    st.setString(3, car.getModel());
                if (car.getYear() <= 0)
                    st.setInt(4, rs.getInt(5));
                else
                    st.setInt(4, car.getYear());
                if (car.getEngineCapacity() <= 0)
                    st.setInt(5, rs.getInt(6));
                else
                    st.setInt(5, car.getEngineCapacity());
                if (car.getColor().isEmpty())
                    st.setString(6, rs.getString(7));
                else
                    st.setString(6, car.getColor());
                if (car.getPrice() <= 0)
                    st.setDouble(7, rs.getDouble(8));
                else
                    st.setDouble(7, car.getPrice());
                if (car.getDoors() <= 0)
                    st.setInt(8, rs.getInt(9));
                else
                    st.setInt(8, car.getDoors());
                if (car.getSize().isEmpty())
                    st.setString(9, rs.getString(10));
                else
                    st.setString(9, car.getSize());
                if (car.getPower() <= 0)
                    st.setInt(10, rs.getInt(11));
                else
                    st.setInt(10, car.getPower());
                st.setBoolean(11, car.isAutomatic());
                if (car.getFuel().isEmpty())
                    st.setString(12, rs.getString(13));
                else
                    st.setString(12, car.getFuel());
                if (car.getImage().isEmpty())
                    st.setString(13, rs.getString(14));
                else
                    st.setString(13, car.getImage());
                st.setString(14, carId.toString());
            }

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCar(UUID carId) {
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM cars WHERE car_id = ?");

            st.setString(1, carId.toString());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
