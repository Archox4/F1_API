package com.example.spring1.util;

import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverDTO {
    
    @JsonProperty("meeting_key")
    private Integer meetingKey;
    
    @JsonProperty("session_key")
    private Integer sessionKey;

    @JsonProperty("broadcast_name")
    private String broadcastName;

    @JsonProperty("driver_number")
    private Short driverNumber;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("headshot_url")
    private String headshotUrl;

    @JsonProperty("name_acronym")
    private String nameAcronym;

    @JsonProperty("team_colour")
    private String teamColour;

    @JsonProperty("team_name")
    private String teamName;
}
