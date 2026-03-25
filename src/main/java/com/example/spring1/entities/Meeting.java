package com.example.spring1.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"Meeting\"")
public class Meeting {
    @Id
    @Column(name = "meeting_key", nullable = false)
    private Short id;

    @Column(name = "meeting_name", nullable = false, length = Integer.MAX_VALUE)
    private String meetingName;

    @Column(name = "meeting_official_name", nullable = false, length = Integer.MAX_VALUE)
    private String meetingOfficialName;

    @Column(name = "location", nullable = false, length = Integer.MAX_VALUE)
    private String location;

    @Column(name = "country_code", nullable = false, length = Integer.MAX_VALUE)
    private String countryCode;

    @Column(name = "country_flag", nullable = false, length = Integer.MAX_VALUE)
    private String countryFlag;

    @Column(name = "circuit_short_name", nullable = false, length = Integer.MAX_VALUE)
    private String circuitShortName;

    @Column(name = "date_start", nullable = false)
    private OffsetDateTime dateStart;

    @Column(name = "date_end", nullable = false)
    private OffsetDateTime dateEnd;

    @Column(name = "year", nullable = false)
    private Short year;

    @OneToMany(mappedBy = "meetingKey")
    @JsonManagedReference
    private Set<Session> sessions = new LinkedHashSet<>();


}