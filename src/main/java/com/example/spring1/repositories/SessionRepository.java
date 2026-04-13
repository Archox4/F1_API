package com.example.spring1.repositories;

import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import com.example.spring1.util.DetailedSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    List<Session> findByMeetingKey(Meeting meeting_key);

    Optional<Session> findSessionById(Short id);

    @Query("SELECT new com.example.spring1.util.DetailedSession(" +
            "m.meetingName, m.meetingOfficialName, m.location, m.countryCode, m.countryFlag, " +
            "m.dateStart, m.dateEnd, m.year, s.id, s.sessionType, s.sessionName, " +
            "s.circuitShortName, m.id) " +
            "FROM Session s " +
            "JOIN s.meetingKey m " +
            "WHERE s.id = :session_key")
    Optional<DetailedSession> findMeetingIdForSession(Short session_key);
}
