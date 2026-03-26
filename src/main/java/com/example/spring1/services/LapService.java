package com.example.spring1.services;

import com.example.spring1.projections.TableProjections;
import com.example.spring1.repositories.LapRepository;
import com.example.spring1.util.DetailedLap;
import com.example.spring1.util.DetailedLapResponse;
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
@RequestMapping("/api/laps")
@RequiredArgsConstructor
public class LapService {

    private final LapRepository lapRepository;

    @GetMapping("/session_key={session_key}&driver_number={driver_number}")
    public List<TableProjections.LapProjection> getDriverBySessionKey(@PathVariable short session_key, SessionStatus sessionStatus, @PathVariable Short driver_number){
        if(lapRepository.findLapBySessionKeyIdAndDriverNumber(session_key, driver_number).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laps not found");
        }
        return  lapRepository.findLapBySessionKeyIdAndDriverNumber(session_key, driver_number);
    }

    @GetMapping("/detailed/session_key={session_key}&driver_number={driver_number}")
    public DetailedLapResponse getLapsForSessionAndDriver(@PathVariable Short session_key, @PathVariable Short driver_number){
        if(lapRepository.findLapBySessionKeyIdAndDriverNumber(session_key, driver_number).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laps not found");
        }
        List<DetailedLap> flatLaps = lapRepository.findCombinedData(session_key, driver_number);

        if (flatLaps.isEmpty()) return null;

        DetailedLap first = flatLaps.get(0);
        DetailedLapResponse response = new DetailedLapResponse();
        response.setDriverNumber(first.getDriverNumber());
        response.setFullName(first.getFullName());
        response.setHeadshotUrl(first.getHeadshotUrl());
        response.setTeamColour(first.getTeamColour());

        List<DetailedLapResponse.LapData> lapList = flatLaps.stream().map(f -> {
            DetailedLapResponse.LapData data = new DetailedLapResponse.LapData();
            data.setLapNumber(f.getLapNumber());
            data.setS1(f.getS1());
            data.setS2(f.getS2());
            data.setS3(f.getS3());
            data.setCompound(f.getCompound());
            data.setTyreAge(f.getTyreAgeAtStart());

            return data;
        }).toList();

        response.setLaps(lapList);
        return response;
    }
}
