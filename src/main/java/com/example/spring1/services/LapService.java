package com.example.spring1.services;

import com.example.spring1.entities.Driver;
import com.example.spring1.projections.TableProjections;
import com.example.spring1.repositories.DriverRepository;
import com.example.spring1.repositories.LapRepository;
import com.example.spring1.util.DetailedLap;
import com.example.spring1.util.DetailedLapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/laps")
@RequiredArgsConstructor
public class LapService {

    private final LapRepository lapRepository;
    private final DriverRepository driverRepository;

    @GetMapping("/session_key={sessionKey}&driver_number={driverNumber}")
    public List<TableProjections.LapProjection> getDriverBySessionKey(@PathVariable short sessionKey, @PathVariable Short driverNumber){
        if(lapRepository.findLapBySessionKeyIdAndDriverNumber(sessionKey, driverNumber).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laps not found");
        }
        return  lapRepository.findLapBySessionKeyIdAndDriverNumber(sessionKey, driverNumber);
    }

    @GetMapping("/detailed/session_key={sessionKey}&driver_number={driverNumber}")
    public ResponseEntity<?> getLapsForSessionAndDriver(@PathVariable Short sessionKey, @PathVariable Short driverNumber){
        return getLapsDataForSessionAndDriver(sessionKey, driverNumber);
    }

    @GetMapping("/detailedForAllDrivers/session_key={sessionKey}")
    public ResponseEntity<?> getLapsForSessionsAllDrivers(@PathVariable Short sessionKey){
        List<Driver> drivers = driverRepository.findDriverBySessionKeyId(sessionKey);
        List<Object> allDriversData = new ArrayList<>();

        if (drivers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "no data for session"));
        };

        for (Driver driver: drivers){
            ResponseEntity<?> response = getLapsDataForSessionAndDriver(sessionKey, driver.getDriverNumber());
            if(response.getStatusCode() != HttpStatus.NOT_FOUND){
                allDriversData.add(response.getBody());
            }
        }
        return ResponseEntity.ok(allDriversData);
    }


    private ResponseEntity<?> getLapsDataForSessionAndDriver(Short sessionKey, Short driverNumber){
        if(lapRepository.findLapBySessionKeyIdAndDriverNumber(sessionKey, driverNumber).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laps not found");
        }
        List<DetailedLap> flatLaps = lapRepository.findCombinedData(sessionKey, driverNumber);
        if (flatLaps.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "no data"));;

        DetailedLap first = flatLaps.get(0);
        DetailedLapResponse response = new DetailedLapResponse();
        response.setDriverNumber(first.getDriverNumber());
        response.setFullName(first.getFullName());
        response.setHeadshotUrl(first.getHeadshotUrl());
        response.setTeamColour(first.getTeamColour());

        List<DetailedLapResponse.LapData> lapList = flatLaps.stream().map(f -> {
            DetailedLapResponse.LapData data = new DetailedLapResponse.LapData();
            data.setLapNumber(f.getLapNumber());
            data.setLapDuration(f.getLapDuration());
            data.setCompound(f.getCompound());
            data.setTyreAge(f.getTyreAgeAtStart());
            data.setDurationSector1(f.getDurationSector1());
            data.setDurationSector2(f.getDurationSector2());
            data.setDurationSector3(f.getDurationSector3());
            data.setIsPitOutLap(f.getIsPitOutLap());
            return data;
        }).toList();

        response.setLaps(lapList);
        return ResponseEntity.ok(response);
    }
}
