package org.example.repositories;

import org.example.models.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Integer> {
    List<ParkingSlot> findByParkedCarColorIgnoreCase(String color);
    ParkingSlot findByParkedCarRegistrationNumber(String registrationNumber);
}
