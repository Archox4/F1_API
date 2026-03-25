package com.example.spring1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "\"Stint\"")
public class Stint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "compound", length = 50)
    private String compound;

    @Column(name = "driver_number", nullable = false)
    private Short driverNumber;

    @Column(name = "lap_start", nullable = true)
    private Short lapStart;

    @Column(name = "lap_end", nullable = true)
    private Short lapEnd;

    @Column(name = "stint_number", nullable = false)
    private Short stintNumber;

    @Column(name = "tyre_age_at_start", nullable = false)
    private Short tyreAgeAtStart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "meeting_key", nullable = false)
    private Meeting meetingKey;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "session_key", nullable = false)
    private Session sessionKey;


}