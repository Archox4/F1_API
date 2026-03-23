package com.example.spring1.services;

import com.example.spring1.repositories.MeetingRepository;
import com.example.spring1.entities.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/races")
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    @GetMapping
    public List<Meeting> getAllRaces(){
        return meetingRepository.findAll();
    }

    @GetMapping("/{meeting_key}")
    public Meeting getRaceById(@PathVariable int meeting_key) {
        return meetingRepository.findById(meeting_key)
                .orElseThrow(() -> new RuntimeException("Race not found with id: " + meeting_key));
    }
}
