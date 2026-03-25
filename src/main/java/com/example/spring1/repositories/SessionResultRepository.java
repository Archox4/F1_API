package com.example.spring1.repositories;

import com.example.spring1.entities.Lap;
import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import com.example.spring1.entities.SessionResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionResultRepository extends JpaRepository<SessionResult, Integer> {
    Optional<SessionResult> findBySessionKeyIdAndMeetingKeyIdAndDriverNumber(Short sessionKey, Short meetingKey, Short driverNumber);
}
