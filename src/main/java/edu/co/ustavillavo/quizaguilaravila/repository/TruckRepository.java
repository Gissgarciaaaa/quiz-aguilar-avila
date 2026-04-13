package edu.co.ustavillavo.quizaguilaravila.repository;

import edu.co.ustavillavo.quizaguilaravila.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TruckRepository extends JpaRepository<Truck, Long> {
    Optional<Truck> findByPlate(String plate);
}