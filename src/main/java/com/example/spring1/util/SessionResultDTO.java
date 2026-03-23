package com.example.spring1.util;

import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionResultDTO {

    @JsonProperty("session_key")
    private Integer sessionKey;

    @JsonProperty("meeting_key")
    private Integer meetingKey;

    @JsonProperty("driver_number")
    private Short driverNumber;

    @JsonProperty("duration")
    private List<Double> duration;

    @JsonProperty("number_of_laps")
    private Short numberOfLaps;

    @JsonProperty("position")
    private Short position;

    @JsonProperty("dnf")
    private Boolean dnf;

    @JsonProperty("dns")
    private Boolean dns;

    @JsonProperty("dsq")
    private Boolean dsq;
}
