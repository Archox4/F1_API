package com.example.spring1.services;

import com.example.spring1.repositories.MeetingRepository;
import com.example.spring1.entities.Meeting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/year/{year}")
    public List<Meeting> getRacesByYear(@PathVariable short year){
        LocalDate date = LocalDate.now();
        if(year < 2023 || year > date.getYear()) return null;
        try {
            List<Meeting> raw_meetings = meetingRepository.findByYear(year);
            List<Meeting> meetings = raw_meetings.stream()
                    .filter(meeting -> meeting.getDateStart().isBefore(OffsetDateTime.now()))
                    .toList();
            if(!meetings.isEmpty()){
                return meetings;
            }
            else {
                return null;
            }
        } catch (Exception e){
            log.error("Error: {}", String.valueOf(e));
        }
        return null;
    }
}
