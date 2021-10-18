package com.rentACar.dao;

import com.rentACar.db.DatabaseConnection;
import com.rentACar.model.ContractModel;
import com.rentACar.model.request.ContractPendingRequestModel;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface ContractDao {
    double getCarPrice(UUID carId, LocalDate startDate, LocalDate endDate);
    void addContract(ContractPendingRequestModel contract);
    void approveContract(UUID contractId);
    void deleteContract(UUID contractId);
    List<LocalDate> calendar(UUID carId);
    List<LocalDate> betweenDates(LocalDate startDate, LocalDate endDate);
    HashMap<String, List<LocalDate>> allBetweenDates();
    List<ContractModel> allUserContracts(UUID userId);
    List<ContractModel> allContracts();
    Connection conn = DatabaseConnection.getConnection();
}
