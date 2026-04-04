package com.example.spring1.util;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class DetailedLap {
    private Short driverNumber;
    private String headshotUrl;
    private String nameAcronym;
    private String teamColour;
    private String fullName;

    private Short lapNumber;
    private Double durationSector1;
    private Double durationSector2;
    private Double durationSector3;
    private Double lapDuration;
    private String isPitOutLap;

    private String compound;
    private Short tyreAgeAtStart;


    public DetailedLap(Short driverNumber, String headshotUrl, String nameAcronym,
                        String teamColour, String fullName, Short lapNumber, Double durationSector1,
                        Double durationSector2, Double durationSector3, Double lapDuration, String isPitOutLap,
                        String compound, Short tyreAgeAtStart) {
        this.driverNumber = driverNumber;
        this.headshotUrl = headshotUrl;
        this.nameAcronym = nameAcronym;
        this.teamColour = teamColour;
        this.fullName = fullName;
        this.lapNumber = lapNumber;
        this.durationSector1 = durationSector1;
        this.durationSector2 = durationSector2;
        this.durationSector3 = durationSector3;
        this.lapDuration = Objects.requireNonNullElse(lapDuration, 0.0);
        this.isPitOutLap = isPitOutLap;
        this.compound = compound;
        this.tyreAgeAtStart = tyreAgeAtStart;
    }
}
