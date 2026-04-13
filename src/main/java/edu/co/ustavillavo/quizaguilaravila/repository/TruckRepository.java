package com.quiz.repository;

import com.quiz.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckRepository extends JpaRepository<Truck, Long> {
}