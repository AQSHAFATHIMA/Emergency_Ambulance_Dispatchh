package com.example.ambulance;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AmbulanceServiceTest {

    private Driver createDriver(
            String id) {

        return new Driver(
                id,
                "Driver " + id,
                "9876543210");
    }

    private Ambulance createAmbulance(
            String id,
            AmbulanceType type,
            double distance) {

        return new Ambulance(
                id,
                type,
                createDriver("D" + id),
                distance,
                40);
    }

    private EmergencyRequest createRequest(
            String id,
            EmergencyLevel level,
            AmbulanceType type) {

        return new EmergencyRequest(
                id,
                "PAT-" + id,
                "Chennai",
                "Apollo Hospital",
                level,
                type);
    }

    @Test
    public void testNearestAmbulanceSelected()
            throws Exception {

        AmbulanceService service =
                new AmbulanceService();

        service.addAmbulance(
                createAmbulance(
                        "A1",
                        AmbulanceType.BASIC,
                        10));

        service.addAmbulance(
                createAmbulance(
                        "A2",
                        AmbulanceType.BASIC,
                        5));

        EmergencyRecord record =
                service.acceptEmergency(
                        createRequest(
                                "R1",
                                EmergencyLevel.HIGH,
                                AmbulanceType.BASIC));

        assertNotNull(
                record.getAmbulance());

        assertEquals(
                "A2",
                record.getAmbulance()
                        .getAmbulanceId());
    }

    @Test
    public void testAmbulanceTypeMatching()
            throws Exception {

        AmbulanceService service =
                new AmbulanceService();

        service.addAmbulance(
                createAmbulance(
                        "A1",
                        AmbulanceType.BASIC,
                        2));

        service.addAmbulance(
                createAmbulance(
                        "A2",
                        AmbulanceType.ICU,
                        10));

        EmergencyRecord record =
                service.acceptEmergency(
                        createRequest(
                                "R1",
                                EmergencyLevel.CRITICAL,
                                AmbulanceType.ICU));

        assertEquals(
                AmbulanceType.ICU,
                record.getAmbulance()
                        .getType());
    }

    @Test
    public void testWaitingQueue()
            throws Exception {

        AmbulanceService service =
                new AmbulanceService();

        service.addAmbulance(
                createAmbulance(
                        "A1",
                        AmbulanceType.BASIC,
                        5));

        service.acceptEmergency(
                createRequest(
                        "R1",
                        EmergencyLevel.HIGH,
                        AmbulanceType.BASIC));

        service.acceptEmergency(
                createRequest(
                        "R2",
                        EmergencyLevel.NORMAL,
                        AmbulanceType.BASIC));

        assertEquals(
                1,
                service.getWaitingQueueSize());
    }

    @Test
    public void testNoSuitableAmbulance()
            throws Exception {

        AmbulanceService service =
                new AmbulanceService();

        EmergencyRecord record =
                service.acceptEmergency(
                        createRequest(
                                "R1",
                                EmergencyLevel.CRITICAL,
                                AmbulanceType.ICU));

        assertNull(
                record.getAmbulance());

        assertEquals(
                1,
                service.getWaitingQueueSize());
    }

    @Test
    public void testETA()
            throws Exception {

        Ambulance ambulance =
                createAmbulance(
                        "A1",
                        AmbulanceType.BASIC,
                        10);

        double eta =
                ambulance.calculateETA();

        assertEquals(
                15.0,
                eta,
                0.001);
    }

    @Test
    public void testCompleteAmbulanceStateFlow()
            throws Exception {

        AmbulanceService service =
                new AmbulanceService();

        service.addAmbulance(
                createAmbulance(
                        "A1",
                        AmbulanceType.BASIC,
                        5));

        service.acceptEmergency(
                createRequest(
                        "R1",
                        EmergencyLevel.HIGH,
                        AmbulanceType.BASIC));

        Ambulance ambulance =
                service.getRecord("R1")
                        .getAmbulance();

        assertEquals(
                AmbulanceStatus.DISPATCHED,
                ambulance.getStatus());

        service.startJourney("R1");

        assertEquals(
                AmbulanceStatus.EN_ROUTE,
                ambulance.getStatus());

        service.pickUpPatient("R1");

        assertEquals(
                AmbulanceStatus.PATIENT_PICKED_UP,
                ambulance.getStatus());

        service.arriveAtHospital("R1");

        assertEquals(
                AmbulanceStatus.HOSPITAL_ARRIVED,
                ambulance.getStatus());

        service.makeAmbulanceAvailable("R1");

        assertEquals(
                AmbulanceStatus.AVAILABLE,
                ambulance.getStatus());
    }

    @Test
    public void testDuplicateRequestRejected()
            throws Exception {

        AmbulanceService service =
                new AmbulanceService();

        EmergencyRequest request =
                createRequest(
                        "R1",
                        EmergencyLevel.HIGH,
                        AmbulanceType.BASIC);

        service.acceptEmergency(request);

        assertThrows(
                EmergencyException.class,
                () -> service.acceptEmergency(
                        request));
    }

    @Test
    public void testDuplicateAmbulanceRejected()
            throws Exception {

        AmbulanceService service =
                new AmbulanceService();

        Ambulance ambulance =
                createAmbulance(
                        "A1",
                        AmbulanceType.BASIC,
                        5);

        service.addAmbulance(ambulance);

        assertThrows(
                EmergencyException.class,
                () -> service.addAmbulance(
                        ambulance));
    }

    @Test
    public void testInvalidDistance() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Ambulance(
                        "A1",
                        AmbulanceType.BASIC,
                        createDriver("D1"),
                        -5,
                        40));
    }

    @Test
    public void testInvalidSpeed() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Ambulance(
                        "A1",
                        AmbulanceType.BASIC,
                        createDriver("D1"),
                        5,
                        0));
    }

    @Test
    public void testInvalidDriver() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Driver(
                        "",
                        "Arun",
                        "9876543210"));
    }

    @Test
    public void testInvalidStateTransition() {

        Ambulance ambulance =
                createAmbulance(
                        "A1",
                        AmbulanceType.BASIC,
                        5);

        assertThrows(
                IllegalStateException.class,
                ambulance::pickUpPatient);
    }

    @Test
    public void testEmergencyHistory()
            throws Exception {

        AmbulanceService service =
                new AmbulanceService();

        service.addAmbulance(
                createAmbulance(
                        "A1",
                        AmbulanceType.BASIC,
                        5));

        service.acceptEmergency(
                createRequest(
                        "R1",
                        EmergencyLevel.CRITICAL,
                        AmbulanceType.BASIC));

        service.acceptEmergency(
                createRequest(
                        "R2",
                        EmergencyLevel.HIGH,
                        AmbulanceType.BASIC));

        List<EmergencyRecord> history =
                service.getEmergencyHistory();

        assertEquals(
                2,
                history.size());
    }

    @Test
    public void testAllEmergencyLevels() {

        assertEquals(
                4,
                EmergencyLevel.CRITICAL
                        .getPriority());

        assertEquals(
                3,
                EmergencyLevel.HIGH
                        .getPriority());

        assertEquals(
                2,
                EmergencyLevel.MODERATE
                        .getPriority());

        assertEquals(
                1,
                EmergencyLevel.NORMAL
                        .getPriority());
    }
}
