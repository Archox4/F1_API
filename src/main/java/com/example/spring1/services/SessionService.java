package com.example.spring1.services;

import com.example.spring1.repositories.SessionRepository;
import com.example.spring1.entities.Session;
import com.example.spring1.util.DetailedSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @GetMapping("/detailed/{session_key}")
    public ResponseEntity<?> getRaceDataBySessionKey(@PathVariable short session_key){
        Optional<DetailedSession> sessionData = sessionRepository.findMeetingIdForSession(session_key);
        if(sessionData.isPresent()){
            return ResponseEntity.ok(sessionData.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "no data for session"));
    }

}
