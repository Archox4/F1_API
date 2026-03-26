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
        private Short s1, s2, s3;
        private String compound;
        private Short tyreAge;
    }
}
