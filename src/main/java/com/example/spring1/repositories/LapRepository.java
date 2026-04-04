package com.example.spring1.repositories;

import com.example.spring1.entities.Lap;
import com.example.spring1.entities.Session;
import com.example.spring1.util.DetailedLap;
import com.example.spring1.projections.TableProjections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LapRepository extends JpaRepository<Lap, Integer> {
    Optional<Lap> findByDriverNumberAndSessionKeyAndLapNumber(Short driverNumber, Session sessionKey, Short lapNumber);

    List<TableProjections.LapProjection> findLapBySessionKeyIdAndDriverNumber(Short sessionKeyId, Short driverNumber);

    @Query("SELECT new com.example.spring1.util.DetailedLap(" +
            "l.driverNumber, d.headshotUrl, d.nameAcronym, d.teamColour, d.fullName, l.lapNumber, " +
            "l.durationSector1, l.durationSector2, l.durationSector3 ,l.lapDuration, l.isPitOutLap, " +
            "s.compound, s.tyreAgeAtStart) " +
            "FROM Lap l " +
            "JOIN Driver d ON l.driverNumber = d.driverNumber AND l.sessionKey = d.sessionKey " +
            "JOIN Stint s ON l.driverNumber = s.driverNumber AND l.sessionKey = s.sessionKey " +
            "WHERE l.sessionKey.id = :sessionKeyId " +
            "AND d.driverNumber = :driverNumber " +
            "AND l.lapNumber BETWEEN s.lapStart AND s.lapEnd")
    List<DetailedLap> findCombinedData(Short sessionKeyId, Short driverNumber);
}
