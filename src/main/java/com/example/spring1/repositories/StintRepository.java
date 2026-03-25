package com.example.spring1.repositories;

import com.example.spring1.entities.Lap;
import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import com.example.spring1.entities.Stint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StintRepository extends JpaRepository<Stint, Integer> {
    Optional<Stint> findByLapStartAndDriverNumberAndSessionKeyId(Short lapStart, Short driver_number, Short sessionKey_id);
}

