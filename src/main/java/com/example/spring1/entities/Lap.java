package com.example.spring1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "\"Lap\"")
public class Lap {
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

    @Column(name = "lap_number", nullable = false)
    private Short lapNumber;

    @Column(name = "duration_sector_1", nullable = true)
    private Short durationSector1;

    @Column(name = "duration_sector_2", nullable = true)
    private Short durationSector2;

    @Column(name = "duration_sector_3", nullable = true)
    private Short durationSector3;

    @Column(name = "is_pit_out_lap", nullable = false, length = 5)
    private String isPitOutLap;

    @Column(name = "lap_duration", nullable = true)
    private Double lapDuration;


}