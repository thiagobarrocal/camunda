package com.example.workflow.utils;

public enum TravelStatusEnum {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    DENIED("DENIED");

    private final String status;

    TravelStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
