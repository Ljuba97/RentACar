package com.rentACar.model.response;

public class AdminUpdateUserResponseModel {
    private boolean successful;
    private String info;

    public AdminUpdateUserResponseModel(boolean successful, String info) {
        this.successful = successful;
        this.info = info;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getInfo() {
        return info;
    }
}
