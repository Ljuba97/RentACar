package com.rentACar.model.request;

public class ContractApprovalRequestModel {
    private boolean approved;

    public ContractApprovalRequestModel(boolean approved) {
        this.approved = approved;
    }

    public ContractApprovalRequestModel() {

    }

    public boolean isApproved() {
        return approved;
    }
}
