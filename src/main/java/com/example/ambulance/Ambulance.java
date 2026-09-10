package com.example.ambulance;

public class Ambulance {

    private final String ambulanceId;
    private final AmbulanceType type;
    private final Driver driver;
    private final double distanceFromEmergency;
    private final double averageSpeed;

    private AmbulanceStatus status;

    public Ambulance(
            String ambulanceId,
            AmbulanceType type,
            Driver driver,
            double distanceFromEmergency,
            double averageSpeed) {

        if (ambulanceId == null || ambulanceId.isBlank()) {
            throw new IllegalArgumentException(
                    "Ambulance ID cannot be empty");
        }

        if (type == null) {
            throw new IllegalArgumentException(
                    "Ambulance type is required");
        }

        if (driver == null) {
            throw new IllegalArgumentException(
                    "Driver is required");
        }

        if (distanceFromEmergency < 0) {
            throw new IllegalArgumentException(
                    "Distance cannot be negative");
        }

        if (averageSpeed <= 0) {
            throw new IllegalArgumentException(
                    "Average speed must be greater than zero");
        }

        this.ambulanceId = ambulanceId;
        this.type = type;
        this.driver = driver;
        this.distanceFromEmergency = distanceFromEmergency;
        this.averageSpeed = averageSpeed;
        this.status = AmbulanceStatus.AVAILABLE;
    }

    public String getAmbulanceId() {
        return ambulanceId;
    }

    public AmbulanceType getType() {
        return type;
    }

    public Driver getDriver() {
        return driver;
    }

    public double getDistanceFromEmergency() {
        return distanceFromEmergency;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public AmbulanceStatus getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == AmbulanceStatus.AVAILABLE;
    }

    public double calculateETA() {
        return (distanceFromEmergency / averageSpeed) * 60;
    }

    public void dispatch() {
        if (status != AmbulanceStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Ambulance is not available");
        }

        status = AmbulanceStatus.DISPATCHED;
    }

    public void startJourney() {

        if (status != AmbulanceStatus.DISPATCHED) {
            throw new IllegalStateException(
                    "Ambulance must be dispatched first");
        }

        status = AmbulanceStatus.EN_ROUTE;
    }

    public void pickUpPatient() {

        if (status != AmbulanceStatus.EN_ROUTE) {
            throw new IllegalStateException(
                    "Ambulance must be en route");
        }

        status = AmbulanceStatus.PATIENT_PICKED_UP;
    }

    public void arriveAtHospital() {

        if (status != AmbulanceStatus.PATIENT_PICKED_UP) {
            throw new IllegalStateException(
                    "Patient must be picked up first");
        }

        status = AmbulanceStatus.HOSPITAL_ARRIVED;
    }

    public void makeAvailable() {

        if (status != AmbulanceStatus.HOSPITAL_ARRIVED) {
            throw new IllegalStateException(
                    "Ambulance must arrive at hospital first");
        }

        status = AmbulanceStatus.AVAILABLE;
    }

    @Override
    public String toString() {

        return "Ambulance ID: " + ambulanceId
                + " | Type: " + type
                + " | Driver: " + driver
                + " | Distance: "
                + distanceFromEmergency + " km"
                + " | Status: " + status;
    }
}
