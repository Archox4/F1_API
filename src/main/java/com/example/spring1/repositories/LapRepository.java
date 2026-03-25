package com.example.spring1.repositories;

import com.example.spring1.entities.Lap;
import com.example.spring1.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LapRepository extends JpaRepository<Lap, Integer> {
    Optional<Lap> findByDriverNumberAndSessionKeyAndLapNumber(Short driverNumber, Session sessionKey, Short lapNumber);
}
