package com.rentACar.dao;

import com.rentACar.db.DatabaseConnection;
import com.rentACar.model.CarModel;
import com.rentACar.model.request.CarAddRequestModel;
import com.rentACar.model.request.CarUpdateRequestModel;
import com.rentACar.model.response.CarResponseModel;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface CarDao {
    CarResponseModel getCarById(UUID carId);
    List<CarResponseModel> allCars();
    List<CarResponseModel> searchAvailableCars(LocalDate startDate, LocalDate endDate, String make, String model, int year, boolean automatic, double price, int power, int doors, HashSet<UUID> unavailableCarId);
    List<CarResponseModel> searchCars(String make, String model, int year, boolean automatic, double price, int power, int doors);
    void addCar(CarAddRequestModel car);
    void updateCar(CarUpdateRequestModel car, UUID carId);
    void deleteCar(UUID carId);
    Connection conn = DatabaseConnection.getConnection();
}
