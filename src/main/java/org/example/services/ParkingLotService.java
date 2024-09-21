package org.example.services;

import org.example.models.Car;
import org.example.models.ParkingLot;
import org.example.models.ParkingSlot;
import org.example.repositories.CarRepository;
import org.example.repositories.ParkingLotRepository;
import org.example.repositories.ParkingSlotRepository;
import org.example.exceptions.ParkingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingLotService {

    private final CarRepository carRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingLotRepository parkingLotRepository;

    private ParkingLot defaultParkingLot;

    @Autowired
    public ParkingLotService(CarRepository carRepository, ParkingSlotRepository parkingSlotRepository, ParkingLotRepository parkingLotRepository) {
        this.carRepository = carRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

//    @PostConstruct
//    public void init() {
//        defaultParkingLot = parkingLotRepository.findByName("Default Parking Lot");
//        if (defaultParkingLot == null) {
//            defaultParkingLot = createParkingLot("Default Parking Lot", 1, 10);  // Create a default lot if it doesn't exist
//        }
//    }

    @Transactional
    public ParkingLot createParkingLot(String name, int numberOfFloors, int slotsPerFloor) {
        ParkingLot parkingLot = new ParkingLot(name, numberOfFloors);
        parkingLot = parkingLotRepository.save(parkingLot);

        List<ParkingSlot> slots = new ArrayList<>();
        for (int floor = 1; floor <= numberOfFloors; floor++) {
            for (int slot = 1; slot <= slotsPerFloor; slot++) {
                slots.add(new ParkingSlot(slot, floor, parkingLot));
            }
        }
        parkingSlotRepository.saveAll(slots);

        return parkingLot;
    }

    @Transactional
    public ParkingSlot park(String registrationNumber, String color) throws ParkingException {
        ParkingSlot availableSlot = parkingSlotRepository.findAll().stream()
                .filter(slot -> !slot.isOccupied())
                .findFirst()
                .orElseThrow(() -> new ParkingException("Sorry, parking lot is full"));

        Car car = new Car(registrationNumber, color);
        carRepository.save(car);
        availableSlot.setParkedCar(car);
        return parkingSlotRepository.save(availableSlot);
    }

    @Transactional
    public Car leave(int slotNumber) throws ParkingException {
        ParkingSlot slot = parkingSlotRepository.findById((long) slotNumber)
                .orElseThrow(() -> new ParkingException("Invalid slot number"));

        if (!slot.isOccupied()) {
            throw new ParkingException("No car parked at this slot");
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
        return parkingSlotRepository.findAll().stream()
                .filter(parkingSlot -> parkingSlot.getParkedCar() != null
                        && parkingSlot.getParkedCar().getColor() != null
                        && parkingSlot.getParkedCar().getColor().equalsIgnoreCase(color))
                .map(parkingSlot -> parkingSlot.getParkedCar().getRegistrationNumber())
                .collect(Collectors.toList());
//        return parkingSlotRepository.findByParkingLotIdAndParkedCarColorIgnoreCase(defaultParkingLot.getId(), color).stream()
//                .map(slot -> slot.getParkedCar().getRegistrationNumber())
//                .collect(Collectors.toList());
    }

    public List<Integer> getSlotNumbersByColor(String color) {
        return parkingSlotRepository.findAll().stream()
                .filter(parkingSlot -> parkingSlot.getParkedCar() != null
                        && parkingSlot.getParkedCar().getColor() != null
                        && parkingSlot.getParkedCar().getColor().equalsIgnoreCase(color))
                .map(ParkingSlot::getSlotNumber)
                .collect(Collectors.toList());
//        return parkingSlotRepository.findByParkingLotIdAndParkedCarColorIgnoreCase(defaultParkingLot.getId(), color).stream()
//                .map(ParkingSlot::getSlotNumber)
//                .collect(Collectors.toList());
    }

    public Integer getSlotByRegistration(String registrationNumber) {
        List<Integer> slots = parkingSlotRepository.findAll().stream()
                .filter(parkingSlot -> parkingSlot.getParkedCar() != null
                        && parkingSlot.getParkedCar().getRegistrationNumber().equalsIgnoreCase(registrationNumber))
                .map(ParkingSlot::getSlotNumber)
                .collect(Collectors.toList());
        return (!slots.isEmpty()) ? slots.get(0) : null;
    }
}