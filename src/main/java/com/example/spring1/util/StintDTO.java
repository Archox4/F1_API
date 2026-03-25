package com.example.spring1.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StintDTO {

    @JsonProperty("session_key")
    private Integer sessionKey;

    @JsonProperty("meeting_key")
    private Integer meetingKey;

    @JsonProperty("driver_number")
    private Short driverNumber;

    @JsonProperty("lap_start")
    private Short lapStart;

    @JsonProperty("lap_end")
    private Short lapEnd;

    @JsonProperty("stint_number")
    private Short stintNumber;

    @JsonProperty("tyre_age_at_start")
    private Short tyreAgeAtStart;

    @JsonProperty("compound")
    private String compound;


}
