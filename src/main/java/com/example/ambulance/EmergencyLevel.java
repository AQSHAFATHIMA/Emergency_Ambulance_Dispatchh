package com.example.ambulance;

public enum EmergencyLevel {

    CRITICAL(4),
    HIGH(3),
    MODERATE(2),
    NORMAL(1);

    private final int priority;

    EmergencyLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
