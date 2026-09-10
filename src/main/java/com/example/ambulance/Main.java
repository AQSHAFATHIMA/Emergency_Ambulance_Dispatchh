package com.example.ambulance;

public class Main {

    public static void main(String[] args)
            throws Exception {

        AmbulanceService service =
                new AmbulanceService();

        Driver driver1 =
                new Driver(
                        "D101",
                        "Arun Kumar",
                        "9876543210");

        Driver driver2 =
                new Driver(
                        "D102",
                        "Rahul Kumar",
                        "9876543211");

        Driver driver3 =
                new Driver(
                        "D103",
                        "Vijay Kumar",
                        "9876543212");

        Ambulance ambulance1 =
                new Ambulance(
                        "AMB101",
                        AmbulanceType.BASIC,
                        driver1,
                        10,
                        40);

        Ambulance ambulance2 =
                new Ambulance(
                        "AMB102",
                        AmbulanceType.BASIC,
                        driver2,
                        5,
                        50);

        Ambulance ambulance3 =
                new Ambulance(
                        "AMB103",
                        AmbulanceType.ICU,
                        driver3,
                        8,
                        40);

        service.addAmbulance(ambulance1);
        service.addAmbulance(ambulance2);
        service.addAmbulance(ambulance3);

        EmergencyRequest request1 =
                new EmergencyRequest(
                        "REQ001",
                        "PAT001",
                        "Chennai Central",
                        "Apollo Hospital",
                        EmergencyLevel.CRITICAL,
                        AmbulanceType.BASIC);

        EmergencyRequest request2 =
                new EmergencyRequest(
                        "REQ002",
                        "PAT002",
                        "T Nagar",
                        "MIOT Hospital",
                        EmergencyLevel.HIGH,
                        AmbulanceType.ICU);

        EmergencyRecord record1 =
                service.acceptEmergency(request1);

        EmergencyRecord record2 =
                service.acceptEmergency(request2);

        System.out.println(
                "\n===== DISPATCH RESULTS =====");

        System.out.println(record1);
        System.out.println(record2);

        System.out.println(
                "\n===== AMBULANCE STATUS =====");

        service.displayAmbulances();

        System.out.println(
                "\n===== UPDATING REQ001 =====");

        service.startJourney("REQ001");

        System.out.println(
                "Status: "
                        + service.getRecord("REQ001")
                        .getStatus());

        service.pickUpPatient("REQ001");

        System.out.println(
                "Status: "
                        + service.getRecord("REQ001")
                        .getStatus());

        service.arriveAtHospital("REQ001");

        System.out.println(
                "Status: "
                        + service.getRecord("REQ001")
                        .getStatus());

        service.makeAmbulanceAvailable(
                "REQ001");

        System.out.println(
                "Status: "
                        + service.getRecord("REQ001")
                        .getStatus());

        service.displayEmergencyHistory();

        System.out.println(
                "\nWaiting Requests: "
                        + service.getWaitingQueueSize());
    }
}
