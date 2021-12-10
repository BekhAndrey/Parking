package com.bekh.parking.service;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.ParkingLot;
import com.bekh.parking.model.Vehicle;
import com.bekh.parking.repository.OrderRepository;
import com.bekh.parking.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service("parkingLotService")
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public List<ParkingLot> findAll() {
        return (List<ParkingLot>) parkingLotRepository.findAll();
    }

    public ParkingLot findById(Long id) {
        return parkingLotRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the resource"));
    }

    public void save(ParkingLot parkingLot) {
        parkingLotRepository.save(parkingLot);
    }

    public void delete(ParkingLot parkingLot) {
        parkingLotRepository.delete(parkingLot);
    }

    public List<ParkingLot> findAllCurrentlyParked(LocalDate date) {
        return parkingLotRepository.findAllCurrentlyParked(date);
    }

    public List<ParkingLot> findCurrentlyParkedByVehicleNumber(LocalDate date, String carNumber) {
        return parkingLotRepository.findCurrentlyParkedByVehicleNumber(date, carNumber);
    }

    public List<ParkingLot> findAllByVehicle(Vehicle vehicle) {
        return parkingLotRepository.findAllByVehicle(vehicle);
    }
}
