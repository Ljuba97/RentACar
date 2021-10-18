package com.rentACar.model.response;

public class CarUpdateResponseModel {
    private boolean successful;
    private String message;

    public CarUpdateResponseModel(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }
}
