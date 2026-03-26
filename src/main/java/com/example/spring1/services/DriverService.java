package com.example.spring1.services;

import com.example.spring1.entities.Driver;
import com.example.spring1.entities.Session;
import com.example.spring1.projections.TableProjections;
import com.example.spring1.repositories.DriverRepository;
import com.example.spring1.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final SessionRepository sessionRepository;

    @GetMapping("/{session_key}")
    public List<TableProjections.DriverProjection> getDriverBySessionKey(@PathVariable short session_key, SessionStatus sessionStatus){
        if(sessionRepository.findSessionById(session_key).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found");
        }
        return  driverRepository.findDriversBySessionKeyId(session_key);
    }

//    @GetMapping("/{race_id}")
//    public List<Session> getSessionsByRaceId(@PathVariable int race_id){
//        return  sessionRepository.findByRaceId(race_id);
//    }
}
