package com.bekh.parking.service;

import com.bekh.parking.model.Vehicle;
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
        return vehicleRepository.findAllByDeleted(false);
    }

    public Vehicle findById(Long id) {
        return vehicleRepository.findByIdAndDeleted(id, false).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the resource"));
    }

    public void save(Vehicle vehicle){
        vehicleRepository.save(vehicle);
    }

    public void delete(Vehicle vehicle) {
        vehicle.setDeleted(true);
        vehicleRepository.save(vehicle);
    }

    public List<Vehicle> findAllByOwnerId(Long id) {
        return vehicleRepository.findAllByOwnerIdAndDeleted(id, false);
    }

    public Vehicle findByVehicleNumber(String vehicleNumber) {
        return vehicleRepository.findByVehicleNumberAndDeleted(vehicleNumber, false);
    }
}
