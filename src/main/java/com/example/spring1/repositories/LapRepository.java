package com.example.spring1.repositories;

import com.example.spring1.entities.Lap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LapRepository extends JpaRepository<Lap, Integer> {

}
