package com.example.spring1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "\"Session\"")
public class Session {
    @Id
    @Column(name = "session_key", nullable = false)
    private Short id;

    @Column(name = "session_type", nullable = false, length = Integer.MAX_VALUE)
    private String sessionType;

    @Column(name = "session_name", nullable = false, length = Integer.MAX_VALUE)
    private String sessionName;

    @Column(name = "date_start", nullable = false)
    private OffsetDateTime dateStart;

    @Column(name = "date_end", nullable = false)
    private OffsetDateTime dateEnd;

    @Column(name = "circuit_short_name", nullable = false, length = Integer.MAX_VALUE)
    private String circuitShortName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "meeting_key", nullable = false)
    @JsonBackReference
    private Meeting meetingKey;


}