package com.rentACar.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class ContractModel {
    private UUID contractId, userId, carId;
    private LocalDate startDate, endDate;
    private double totalPrice;
    private boolean signed, approved;

    public ContractModel(UUID contractId, UUID userId, UUID carId, LocalDate startDate, LocalDate endDate, double totalPrice, boolean signed, boolean approved) {
        this.contractId = contractId;
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.signed = signed;
        this.approved = approved;
    }

    public UUID getContractId() {
        return contractId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getCarId() {
        return carId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public boolean isSigned() {
        return signed;
    }

    public boolean isApproved() {
        return approved;
    }
}
