package org.example.models;

import javax.persistence.*;

@Entity
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int slotNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_registration_number", referencedColumnName = "registrationNumber")
    private Car parkedCar;

    public ParkingSlot(){}
    public ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.parkedCar = null;
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
