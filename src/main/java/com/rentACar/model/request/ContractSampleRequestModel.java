package com.rentACar.model.request;

import java.time.LocalDate;
import java.util.UUID;

public class ContractSampleRequestModel {
    private UUID userId, carId;
    private LocalDate startDate, endDate;

    public ContractSampleRequestModel(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
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
}
