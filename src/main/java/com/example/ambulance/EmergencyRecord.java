package com.example.ambulance;

public class EmergencyRecord {

    private final EmergencyRequest request;

    private Ambulance ambulance;

    private String status;

    private double estimatedArrivalTime;

    public EmergencyRecord(
            EmergencyRequest request) {

        this.request = request;
        this.status = "WAITING";
        this.estimatedArrivalTime = -1;
    }

    public EmergencyRequest getRequest() {
        return request;
    }

    public Ambulance getAmbulance() {
        return ambulance;
    }

    public String getStatus() {
        return status;
    }

    public double getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void assignAmbulance(
            Ambulance ambulance) {

        this.ambulance = ambulance;
        this.status = "DISPATCHED";
        this.estimatedArrivalTime =
                ambulance.calculateETA();
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {

        String ambulanceId =
                ambulance == null
                        ? "Not Assigned"
                        : ambulance.getAmbulanceId();

        String eta =
                estimatedArrivalTime < 0
                        ? "N/A"
                        : String.format(
                                "%.2f minutes",
                                estimatedArrivalTime);

        return "Request ID: "
                + request.getRequestId()
                + " | Patient: "
                + request.getPatientId()
                + " | Emergency: "
                + request.getEmergencyLevel()
                + " | Ambulance: "
                + ambulanceId
                + " | Status: "
                + status
                + " | ETA: "
                + eta;
    }
}
