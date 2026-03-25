package com.example.spring1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "\"SessionResult\"")
public class SessionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "session_key", nullable = false)
    private Session sessionKey;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "meeting_key", nullable = false)
    private Meeting meetingKey;

    @Column(name = "driver_number", nullable = false)
    private Short driverNumber;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "duration")
    private Double[] duration;

    @Column(name = "number_of_laps", nullable = false)
    private Short numberOfLaps;

    @Column(name = "\"position\"", nullable = true)
    private Short position;

    @Column(name = "dnf", nullable = false)
    private Boolean dnf;

    @Column(name = "dns", nullable = false)
    private Boolean dns;

    @Column(name = "dsq", nullable = false)
    private Boolean dsq;


}