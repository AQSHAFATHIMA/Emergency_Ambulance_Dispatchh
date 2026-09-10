package com.example.ambulance;

public class Driver {

    private final String driverId;
    private final String driverName;
    private final String phoneNumber;

    public Driver(
            String driverId,
            String driverName,
            String phoneNumber) {

        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException(
                    "Driver ID cannot be empty");
        }

        if (driverName == null || driverName.isBlank()) {
            throw new IllegalArgumentException(
                    "Driver name cannot be empty");
        }

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Phone number cannot be empty");
        }

        this.driverId = driverId;
        this.driverName = driverName;
        this.phoneNumber = phoneNumber;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return driverName + " (" + driverId + ")";
    }
}
