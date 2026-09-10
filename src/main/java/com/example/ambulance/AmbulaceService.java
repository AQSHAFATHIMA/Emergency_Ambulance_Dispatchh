package com.example.ambulance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AmbulanceService {

    private final List<Ambulance> ambulances =
            new ArrayList<>();

    private final PriorityQueue<EmergencyRequest>
            waitingQueue =
            new PriorityQueue<>(
                    Comparator
                            .comparingInt(
                                    (EmergencyRequest r) ->
                                            r.getEmergencyLevel()
                                             .getPriority())
                            .reversed()
            );

    private final Map<String, EmergencyRecord>
            history =
            new LinkedHashMap<>();

    private final Set<String> requestIds =
            new HashSet<>();

    public void addAmbulance(
            Ambulance ambulance)
            throws EmergencyException {

        if (ambulance == null) {
            throw new EmergencyException(
                    "Ambulance cannot be null");
        }

        for (Ambulance existing : ambulances) {

            if (existing.getAmbulanceId()
                    .equals(ambulance.getAmbulanceId())) {

                throw new EmergencyException(
                        "Duplicate ambulance ID: "
                                + ambulance.getAmbulanceId());
            }
        }

        ambulances.add(ambulance);

        processWaitingQueue();
    }

    public EmergencyRecord acceptEmergency(
            EmergencyRequest request)
            throws EmergencyException {

        if (request == null) {
            throw new EmergencyException(
                    "Emergency request cannot be null");
        }

        if (requestIds.contains(
                request.getRequestId())) {

            throw new EmergencyException(
                    "Duplicate emergency request: "
                            + request.getRequestId());
        }

        requestIds.add(request.getRequestId());

        EmergencyRecord record =
                new EmergencyRecord(request);

        history.put(
                request.getRequestId(),
                record);

        waitingQueue.add(request);

        processWaitingQueue();

        return record;
    }

    public void processWaitingQueue() {

        boolean assigned;

        do {

            assigned = false;

            EmergencyRequest selectedRequest = null;
            Ambulance selectedAmbulance = null;

            for (EmergencyRequest request
                    : waitingQueue) {

                Ambulance ambulance =
                        findBestAmbulance(request);

                if (ambulance != null) {

                    selectedRequest = request;
                    selectedAmbulance = ambulance;

                    break;
                }
            }

            if (selectedRequest != null) {

                waitingQueue.remove(
                        selectedRequest);

                selectedAmbulance.dispatch();

                EmergencyRecord record =
                        history.get(
                                selectedRequest
                                        .getRequestId());

                record.assignAmbulance(
                        selectedAmbulance);

                assigned = true;
            }

        } while (assigned);
    }

    private Ambulance findBestAmbulance(
            EmergencyRequest request) {

        Ambulance best = null;

        for (Ambulance ambulance
                : ambulances) {

            if (!ambulance.isAvailable()) {
                continue;
            }

            if (ambulance.getType()
                    != request
                    .getRequiredAmbulanceType()) {
                continue;
            }

            if (best == null
                    || ambulance
                    .getDistanceFromEmergency()
                    < best
                    .getDistanceFromEmergency()) {

                best = ambulance;
            }
        }

        return best;
    }

    public void startJourney(
            String requestId)
            throws EmergencyException {

        EmergencyRecord record =
                getRecord(requestId);

        Ambulance ambulance =
                requireAmbulance(record);

        ambulance.startJourney();

        record.updateStatus("EN_ROUTE");
    }

    public void pickUpPatient(
            String requestId)
            throws EmergencyException {

        EmergencyRecord record =
                getRecord(requestId);

        Ambulance ambulance =
                requireAmbulance(record);

        ambulance.pickUpPatient();

        record.updateStatus(
                "PATIENT_PICKED_UP");
    }

    public void arriveAtHospital(
            String requestId)
            throws EmergencyException {

        EmergencyRecord record =
                getRecord(requestId);

        Ambulance ambulance =
                requireAmbulance(record);

        ambulance.arriveAtHospital();

        record.updateStatus(
                "HOSPITAL_ARRIVED");
    }

    public void makeAmbulanceAvailable(
            String requestId)
            throws EmergencyException {

        EmergencyRecord record =
                getRecord(requestId);

        Ambulance ambulance =
                requireAmbulance(record);

        ambulance.makeAvailable();

        record.updateStatus("COMPLETED");

        processWaitingQueue();
    }

    public EmergencyRecord getRecord(
            String requestId)
            throws EmergencyException {

        EmergencyRecord record =
                history.get(requestId);

        if (record == null) {

            throw new EmergencyException(
                    "Emergency request not found: "
                            + requestId);
        }

        return record;
    }

    private Ambulance requireAmbulance(
            EmergencyRecord record)
            throws EmergencyException {

        if (record.getAmbulance() == null) {

            throw new EmergencyException(
                    "No ambulance assigned to request");
        }

        return record.getAmbulance();
    }

    public List<EmergencyRecord>
    getEmergencyHistory() {

        return new ArrayList<>(
                history.values());
    }

    public int getWaitingQueueSize() {

        return waitingQueue.size();
    }

    public int getAmbulanceCount() {

        return ambulances.size();
    }

    public void displayEmergencyHistory() {

        System.out.println(
                "\n===== EMERGENCY HISTORY =====");

        for (EmergencyRecord record
                : history.values()) {

            System.out.println(record);
        }
    }

    public void displayAmbulances() {

        System.out.println(
                "\n===== AMBULANCE STATUS =====");

        for (Ambulance ambulance
                : ambulances) {

            System.out.println(ambulance);
        }
    }
}
