package org.example.services;

import org.example.exceptions.ParkingException;
import org.example.models.Car;
import org.example.models.ParkingSlot;
import org.example.repositories.CarRepository;
import org.example.repositories.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingLotService {
    private final CarRepository carRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    @Autowired
    public ParkingLotService(CarRepository carRepository, ParkingSlotRepository parkingSlotRepository) {
        this.carRepository = carRepository;
        this.parkingSlotRepository = parkingSlotRepository;
    }

    @Transactional
    public void createParkingLot(int capacity) {
        parkingSlotRepository.deleteAll();
        for (int i = 1; i <= capacity; i++) {
            parkingSlotRepository.save(new ParkingSlot(i));
        }
    }

    @Transactional
    public int park(String registrationNumber, String color) throws ParkingException {
        ParkingSlot availableSlot = parkingSlotRepository.findAll().stream()
                .filter(slot -> !slot.isOccupied())
                .findFirst()
                .orElseThrow(() -> new ParkingException("Sorry, parking lot is full"));

        Car car = new Car(registrationNumber, color);
        carRepository.save(car);
        availableSlot.setParkedCar(car);
        parkingSlotRepository.save(availableSlot);
        return availableSlot.getSlotNumber();
    }

    @Transactional
    public Car leave(int slotNumber) throws ParkingException {
        ParkingSlot slot = parkingSlotRepository.findById(slotNumber)
                .orElseThrow(() -> new ParkingException("Invalid slot number"));

        if (!slot.isOccupied()) {
            throw new ParkingException("No car parked at slot " + slotNumber);
        }

        Car car = slot.removeCar();
        carRepository.delete(car);
        parkingSlotRepository.save(slot);
        return car;
    }

    public List<ParkingSlot> status() {
        return parkingSlotRepository.findAll();
    }

    public List<String> getCarsByColor(String color) {
        return parkingSlotRepository.findByParkedCarColorIgnoreCase(color).stream()
                .map(slot -> slot.getParkedCar().getRegistrationNumber())
                .collect(Collectors.toList());
    }

    public Integer getSlotByRegistration(String registrationNumber) {
        ParkingSlot slot = parkingSlotRepository.findByParkedCarRegistrationNumber(registrationNumber);
        return slot != null ? slot.getSlotNumber() : null;
    }
}
