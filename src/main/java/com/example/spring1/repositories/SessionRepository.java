package com.example.spring1.repositories;

import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    List<Session> findByMeetingKey(Meeting meeting_key);
}
