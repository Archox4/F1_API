package com.example.spring1.services;


import com.example.spring1.repositories.MeetingRepository;
import com.example.spring1.entities.Meeting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllRaces(){
        List<Meeting> meetings = meetingRepository.findAll();
        if (meetings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No meetings found");
        }
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/{meeting_key}")
    public ResponseEntity<?> getRaceById(@PathVariable int meeting_key) {
        Optional<Meeting> meeting = meetingRepository.findById(meeting_key);
        if (meeting.isPresent()) {
            return ResponseEntity.ok(meeting.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No race found with meeting key: " + meeting_key);
        }
    }


    @GetMapping("/year/{year}")
    public ResponseEntity<?> getRacesByYear(@PathVariable short year){
        if(year < 2023 || year > 2025) return null;
        List<Meeting> raw_meetings = meetingRepository.findByYear(year);
        List<Meeting> filtered_meetings = raw_meetings.stream()
                    .filter(meeting -> meeting.getDateStart().isBefore(OffsetDateTime.now()))
                    .toList();

        if (filtered_meetings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No races found for year: " + year);
        }

        return ResponseEntity.ok(filtered_meetings);
    }
}
