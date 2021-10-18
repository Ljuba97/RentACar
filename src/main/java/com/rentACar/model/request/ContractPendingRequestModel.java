package com.rentACar.model.request;

import java.time.LocalDate;
import java.util.UUID;

public class ContractPendingRequestModel {
    private UUID contractId, userId, carId;
    LocalDate startDate, endDate;
    double totalPrice;
    boolean signed, approved;

    public ContractPendingRequestModel(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate, double totalPrice, boolean signed) {
        this.contractId = UUID.randomUUID();
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.signed = signed;
        this.approved = false;
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
