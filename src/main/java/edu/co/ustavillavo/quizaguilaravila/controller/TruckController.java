package edu.co.ustavillavo.quizaguilaravila.controller;

import edu.co.ustavillavo.quizaguilaravila.dto.TruckRequest;
import edu.co.ustavillavo.quizaguilaravila.model.AppUser;
import edu.co.ustavillavo.quizaguilaravila.model.Role;
import edu.co.ustavillavo.quizaguilaravila.model.Truck;
import edu.co.ustavillavo.quizaguilaravila.repository.AppUserRepository;
import edu.co.ustavillavo.quizaguilaravila.repository.TruckRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trucks")
public class TruckController {

    private final TruckRepository truckRepository;
    private final AppUserRepository userRepository;

    public TruckController(TruckRepository truckRepository, AppUserRepository userRepository) {
        this.truckRepository = truckRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Truck> getAll() {
        return truckRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Truck> getById(@PathVariable Long id) {
        return truckRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TruckRequest request) {
        AppUser driver = userRepository.findById(request.driverId())
                .orElseThrow(() -> new RuntimeException("Driver no encontrado"));

        if (driver.getRole() != Role.DRIVER) {
            return ResponseEntity.badRequest().body("El usuario asignado no tiene rol DRIVER");
        }

        Truck truck = new Truck();
        truck.setBrand(request.brand());
        truck.setCapacity(request.capacity());
        truck.setColor(request.color());
        truck.setPlate(request.plate());
        truck.setDriver(driver);

        return ResponseEntity.status(HttpStatus.CREATED).body(truckRepository.save(truck));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TruckRequest request) {
        return truckRepository.findById(id)
                .map(truck -> {
                    AppUser driver = userRepository.findById(request.driverId())
                            .orElseThrow(() -> new RuntimeException("Driver no encontrado"));

                    if (driver.getRole() != Role.DRIVER) {
                        return ResponseEntity.badRequest().body("El usuario asignado no tiene rol DRIVER");
                    }

                    truck.setBrand(request.brand());
                    truck.setCapacity(request.capacity());
                    truck.setColor(request.color());
                    truck.setPlate(request.plate());
                    truck.setDriver(driver);

                    return ResponseEntity.ok(truckRepository.save(truck));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!truckRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        truckRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}