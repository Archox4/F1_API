package com.example.spring1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "\"Driver\"")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "meeting_key", nullable = false)
    private Meeting meetingKey;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "session_key", nullable = false)
    private Session sessionKey;

    @Column(name = "broadcast_name", nullable = false, length = 50)
    private String broadcastName;

    @Column(name = "driver_number", nullable = false)
    private Short driverNumber;

    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(name = "headshot_url", nullable = false)
    private String headshotUrl;

    @Column(name = "name_acronym", nullable = false, length = 4)
    private String nameAcronym;

    @Column(name = "team_colour", nullable = false, length = 10)
    private String teamColour;

    @Column(name = "team_name", nullable = false, length = 70)
    private String teamName;


}