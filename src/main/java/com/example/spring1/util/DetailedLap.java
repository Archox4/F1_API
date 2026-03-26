package com.example.spring1.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailedLap {
    private Short driverNumber;
    private String headshotUrl;
    private String nameAcronym;
    private String teamColour;
    private String fullName;

    private Short lapNumber;
    private Short s1;
    private Short s2;
    private Short s3;
    private String isPitOutLap;

    private String compound;
    private Short tyreAgeAtStart;


    public DetailedLap(Short driverNumber, String headshotUrl, String nameAcronym,
                        String teamColour, String fullName, Short lapNumber,
                        Short s1, Short s2, Short s3, String isPitOutLap,
                        String compound, Short tyreAgeAtStart) {
        this.driverNumber = driverNumber;
        this.headshotUrl = headshotUrl;
        this.nameAcronym = nameAcronym;
        this.teamColour = teamColour;
        this.fullName = fullName;
        this.lapNumber = lapNumber;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.isPitOutLap = isPitOutLap;
        this.compound = compound;
        this.tyreAgeAtStart = tyreAgeAtStart;
    }


}
