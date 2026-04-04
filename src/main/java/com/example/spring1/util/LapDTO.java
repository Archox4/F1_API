package com.example.spring1.util;

import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LapDTO {

    @JsonProperty("session_key")
    private Integer sessionKey;

    @JsonProperty("meeting_key")
    private Integer meetingKey;

    @JsonProperty("driver_number")
    private Short driverNumber;

    @JsonProperty("lap_number")
    private Short lapNumber;

    @JsonProperty("duration_sector_1")
    private Double durationSector1;

    @JsonProperty("duration_sector_2")
    private Double durationSector2;

    @JsonProperty("duration_sector_3")
    private Double durationSector3;

    @JsonProperty("is_pit_out_lap")
    private String isPitOutLap;

    @JsonProperty("lap_duration")
    private Double lapDuration;
}
