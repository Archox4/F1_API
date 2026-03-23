package com.example.spring1.services;

import com.example.spring1.repositories.SessionRepository;
import com.example.spring1.entities.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @GetMapping("/{race_key}")
    public Session getSessionByRaceKey(@PathVariable int race_key){
        return  sessionRepository.findById(race_key).orElseThrow(() -> new RuntimeException("Session not found with key: " + race_key));
    }

//    @GetMapping("/{race_id}")
//    public List<Session> getSessionsByRaceId(@PathVariable int race_id){
//        return  sessionRepository.findByRaceId(race_id);
//    }
}
