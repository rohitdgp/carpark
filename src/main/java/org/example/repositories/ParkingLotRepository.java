package org.example.repositories;

import org.example.models.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
    ParkingLot findByName(String name);
}
