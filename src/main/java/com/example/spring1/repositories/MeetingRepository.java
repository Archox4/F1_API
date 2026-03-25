package com.example.spring1.repositories;

import com.example.spring1.entities.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    List<Meeting> findByYear(Short year);

    Meeting findById(Short meeting_key);

    Meeting findById(Meeting meeting);
}
