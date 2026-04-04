package com.example.spring1.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetailedLapResponse {
    private Short driverNumber;
    private String fullName;
    private String headshotUrl;
    private String teamColour;

    private List<LapData> laps;

    @Getter @Setter
    public static class LapData {
        private Short lapNumber;
        private String isPitOutLap;
        private Double lapDuration;
        private Double durationSector1;
        private Double durationSector2;
        private Double durationSector3;
        private String compound;
        private Short tyreAge;
    }
}
