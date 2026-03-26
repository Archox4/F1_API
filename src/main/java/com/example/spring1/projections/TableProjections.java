package com.example.spring1.projections;

public interface TableProjections {
    interface DriverProjection{
        String getFullName();
        Short getDriverNumber();
        String getBroadcastName();
        String getHeadshotUrl();
        String getNameAcronym();
        String getTeamName();
    }
    interface LapProjection{
        Short getDriverNumber();
        Short getLapNumber();
        Short getDurationSector1();
        Short getDurationSector2();
        Short getDurationSector3();
        String getIsPitOutLap();
        Double getLapDuration();
    }

}
