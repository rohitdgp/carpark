package org.example.models;

import javax.persistence.*;

@Entity
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int slotNumber;
    private int floorNumber;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_registration_number", referencedColumnName = "registrationNumber")
    private Car parkedCar;

    public ParkingSlot() {}

    public ParkingSlot(int slotNumber, int floorNumber, ParkingLot parkingLot) {
        this.slotNumber = slotNumber;
        this.floorNumber = floorNumber;
        this.parkingLot = parkingLot;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public Car getParkedCar() {
        return parkedCar;
    }

    public void setParkedCar(Car parkedCar) {
        this.parkedCar = parkedCar;
    }

    public void parkCar(Car car) {
        this.parkedCar = car;
    }

    public Car removeCar() {
        Car car = this.parkedCar;
        this.parkedCar = null;
        return car;
    }

    public boolean isOccupied() {
        return parkedCar != null;
    }

    @Override
    public String toString() {
        return "ParkingSlot{" +
                "slotNumber=" + slotNumber +
                ", parkedCar=" + parkedCar +
                '}';
    }
}
