package com.bekh.parking.repository;

import com.bekh.parking.model.User;
import com.bekh.parking.model.Vehicle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

    List<Vehicle> findAllByOwnerId(Long id);

    Vehicle findByVehicleNumber(String vehicleNumber);
}
