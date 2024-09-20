package org.example.controllers;

import org.example.exceptions.ParkingException;
import org.example.models.Car;
import org.example.models.ParkingSlot;
import org.example.services.ParkingLotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
@Tag(name = "Parking Lot Management", description = "APIs for managing the parking lot")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @Autowired
    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new parking lot", description = "Creates a new parking lot with the specified capacity")
    @ApiResponse(responseCode = "200", description = "Successfully created parking lot")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<String> createParkingLot(@RequestBody Map<String, Integer> request) {
        int capacity = request.get("capacity");
        parkingLotService.createParkingLot(capacity);
        return ResponseEntity.ok("Parking lot created with capacity " + capacity);
    }

    @PostMapping("/park")
    @Operation(summary = "Park a car", description = "Parks a car in the parking lot")
    @ApiResponse(responseCode = "200", description = "Successfully parked the car")
    @ApiResponse(responseCode = "400", description = "Parking lot is full or invalid input")
    public ResponseEntity<?> parkCar(@RequestBody Car car) {
        try {
            int slotNumber = parkingLotService.park(car.getRegistrationNumber(), car.getColor());
            return ResponseEntity.ok("Parked at slot " + slotNumber);
        } catch (ParkingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/leave/{slotNumber}")
    @Operation(summary = "Remove a car from the parking lot", description = "Removes a car from the specified slot")
    @ApiResponse(responseCode = "200", description = "Successfully removed the car")
    @ApiResponse(responseCode = "400", description = "Invalid slot number or slot is already empty")
    public ResponseEntity<?> leaveParkingSlot(@PathVariable int slotNumber) {
        try {
            Car car = parkingLotService.leave(slotNumber);
            return ResponseEntity.ok("Leave " + car.getRegistrationNumber());
        } catch (ParkingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Get parking lot status", description = "Returns the current status of the parking lot")
    public ResponseEntity<List<ParkingSlot>> getParkingStatus() {
        return ResponseEntity.ok(parkingLotService.status());
    }

    @GetMapping("/cars/color/{color}")
    @Operation(summary = "Get cars by color", description = "Returns a list of registration numbers of cars with the specified color")
    public ResponseEntity<List<String>> getCarsByColor(@PathVariable String color) {
        return ResponseEntity.ok(parkingLotService.getCarsByColor(color));
    }

    @GetMapping("/slot/registration/{registrationNumber}")
    @Operation(summary = "Get slot by registration number", description = "Returns the slot number for the car with the specified registration number")
    @ApiResponse(responseCode = "200", description = "Successfully found the slot")
    @ApiResponse(responseCode = "404", description = "Car not found")
    public ResponseEntity<?> getSlotByRegistration(@PathVariable String registrationNumber) {
        Integer slotNumber = parkingLotService.getSlotByRegistration(registrationNumber);
        if (slotNumber != null) {
            return ResponseEntity.ok("Registration " + registrationNumber + " parked at slot " + slotNumber);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}