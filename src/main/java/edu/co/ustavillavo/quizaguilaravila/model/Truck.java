package com.quiz.model;

import jakarta.persistence.*;

@Entity
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private Integer capacity;
    private String color;

    @Column(unique = true)
    private String plate;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private AppUser driver;

    public Truck() {
    }

    public Truck(Long id, String brand, Integer capacity, String color, String plate, AppUser driver) {
        this.id = id;
        this.brand = brand;
        this.capacity = capacity;
        this.color = color;
        this.plate = plate;
        this.driver = driver;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public AppUser getDriver() {
        return driver;
    }

    public void setDriver(AppUser driver) {
        this.driver = driver;
    }
}