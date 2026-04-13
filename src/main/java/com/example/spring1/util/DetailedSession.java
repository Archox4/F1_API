package com.example.spring1.util;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class DetailedSession {
    private String meetingName;
    private String meetingOfficialName;
    private String location;
    private String countryCode;
    private String countryFlag;
    private OffsetDateTime dateStart;
    private OffsetDateTime dateEnd;
    private Short year;

    private Short id;
    private String sessionType;
    private String sessionName;
    private String circuitShortName;
    private Short meeting_key;


    public DetailedSession(String meetingName, String meetingOfficialName, String location,
                           String countryCode, String countryFlag, OffsetDateTime dateStart,
                           OffsetDateTime dateEnd, Short year, Short id,
                           String sessionType, String sessionName, String circuitShortName, Short meetingKey) {
        this.meetingName = meetingName;
        this.meetingOfficialName = meetingOfficialName;
        this.location = location;
        this.countryCode = countryCode;
        this.countryFlag = countryFlag;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.year = year;
        this.id = id;
        this.sessionType = sessionType;
        this.sessionName = sessionName;
        this.circuitShortName = circuitShortName;
        this.meeting_key = meetingKey;
    }
}
