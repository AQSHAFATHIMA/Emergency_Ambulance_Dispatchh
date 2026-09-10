package com.example.ambulance;

public class EmergencyRequest {

    private final String requestId;
    private final String patientId;
    private final String pickupLocation;
    private final String destinationHospital;
    private final EmergencyLevel emergencyLevel;
    private final AmbulanceType requiredAmbulanceType;

    public EmergencyRequest(
            String requestId,
            String patientId,
            String pickupLocation,
            String destinationHospital,
            EmergencyLevel emergencyLevel,
            AmbulanceType requiredAmbulanceType) {

        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException(
                    "Request ID is required");
        }

        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException(
                    "Patient ID is required");
        }

        if (pickupLocation == null
                || pickupLocation.isBlank()) {
            throw new IllegalArgumentException(
                    "Pickup location is required");
        }

        if (destinationHospital == null
                || destinationHospital.isBlank()) {
            throw new IllegalArgumentException(
                    "Destination hospital is required");
        }

        if (emergencyLevel == null) {
            throw new IllegalArgumentException(
                    "Emergency level is required");
        }

        if (requiredAmbulanceType == null) {
            throw new IllegalArgumentException(
                    "Ambulance type is required");
        }

        this.requestId = requestId;
        this.patientId = patientId;
        this.pickupLocation = pickupLocation;
        this.destinationHospital = destinationHospital;
        this.emergencyLevel = emergencyLevel;
        this.requiredAmbulanceType =
                requiredAmbulanceType;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDestinationHospital() {
        return destinationHospital;
    }

    public EmergencyLevel getEmergencyLevel() {
        return emergencyLevel;
    }

    public AmbulanceType getRequiredAmbulanceType() {
        return requiredAmbulanceType;
    }

    @Override
    public String toString() {

        return "Request ID: " + requestId
                + " | Patient: " + patientId
                + " | Level: " + emergencyLevel
                + " | Ambulance Type: "
                + requiredAmbulanceType;
    }
}
