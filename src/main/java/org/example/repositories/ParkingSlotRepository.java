package org.example.repositories;

import org.example.models.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByParkingLotIdAndParkedCarColorIgnoreCase(Long parkingLotId, String color);
    ParkingSlot findByParkingLotIdAndParkedCarRegistrationNumber(Long parkingLotId, String registrationNumber);
    List<ParkingSlot> findByParkingLotId(Long parkingLotId);
    Optional<ParkingSlot> findByParkingLotIdAndSlotNumber(Long parkingLotId, int slotNumber);
}
