package edu.co.ustavillavo.quizaguilaravila.dto;
public record TruckRequest(
        String brand,
        Integer capacity,
        String color,
        String plate,
        Long driverId
) {
}