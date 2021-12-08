package com.bekh.parking.service;

import com.bekh.parking.model.ParkingType;
import com.bekh.parking.model.Vehicle;
import com.bekh.parking.repository.ParkingTypeRepository;
import com.bekh.parking.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service("vehicleService")
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> findAll() {
        return (List<Vehicle>) vehicleRepository.findAll();
    }

    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the resource"));
    }

    public void save(Vehicle vehicle){
        vehicleRepository.save(vehicle);
    }

    public void delete(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }

    public List<Vehicle> findAllByOwnerId(Long id) {
        return (List<Vehicle>) vehicleRepository.findAllByOwnerId(id);
    }

    public Vehicle findByVehicleNumber(String vehicleNumber) {
        return vehicleRepository.findByVehicleNumber(vehicleNumber);
    }
}
