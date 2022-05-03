package com.bekh.parking.service;

import com.bekh.parking.model.ParkingLot;
import com.bekh.parking.model.Vehicle;
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
        return (List<ParkingLot>) parkingLotRepository.findAllByDeleted(false);
    }

    public ParkingLot findById(Long id) {
        return parkingLotRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the resource"));
    }

    public void save(ParkingLot parkingLot) {
        parkingLotRepository.save(parkingLot);
    }

    public void delete(ParkingLot parkingLot) {
        parkingLot.setDeleted(true);
        parkingLotRepository.save(parkingLot);
    }

    public List<ParkingLot> findAllCurrentlyParked(LocalDate date) {
        return (List<ParkingLot>) parkingLotRepository.findAllCurrentlyParked(date);
    }

    public List<ParkingLot> findCurrentlyParkedByVehicleNumber(LocalDate date, String carNumber) {
        return parkingLotRepository.findCurrentlyParkedByVehicleNumber(date, carNumber);
    }

    public List<ParkingLot> findAllByVehicle(Vehicle vehicle) {
        return parkingLotRepository.findAllByVehicleAndDeleted(vehicle, false);
    }
}
