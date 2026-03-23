package com.example.spring1.util;

import com.example.spring1.entities.Meeting;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionDTO {

    @JsonProperty("session_key")
    private Short id;

    @JsonProperty("session_type")
    private String sessionType;

    @JsonProperty("session_name")
    private String sessionName;

    @JsonProperty("date_start")
    private OffsetDateTime dateStart;

    @JsonProperty("date_end")
    private OffsetDateTime dateEnd;

    @JsonProperty("circuit_short_name")
    private String circuitShortName;

    @JsonProperty("meeting_key")
    private Integer meetingKey;


}