package com.example.spring1.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingDTO {

    @JsonProperty("meeting_key")
    private Short id;

    @JsonProperty("meeting_name")
    private String meetingName;

    @JsonProperty("meeting_official_name")
    private String meetingOfficialName;

    private String location;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("country_flag")
    private String countryFlag;

    @JsonProperty("circuit_short_name")
    private String circuitShortName;

    @JsonProperty("date_start")
    private OffsetDateTime dateStart;

    @JsonProperty("date_end")
    private OffsetDateTime dateEnd;

    private Short year;

}

