package com.example.spring1.repositories;

import com.example.spring1.entities.Driver;
import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    List<Driver> findDriverBySessionKeyAndMeetingKey(Session session, Meeting meeting);

    Optional<Driver> findBySessionKeyAndDriverNumber(Session sessionKey, Short driverNumber);
}