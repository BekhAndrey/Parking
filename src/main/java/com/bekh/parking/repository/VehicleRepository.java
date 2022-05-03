package com.bekh.parking.repository;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.User;
import com.bekh.parking.model.Vehicle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

    Optional<Vehicle> findByIdAndDeleted(Long id, boolean isDeleted);

    List<Vehicle> findAllByDeleted(boolean deleted);

    List<Vehicle> findAllByOwnerIdAndDeleted(Long id, boolean isDeleted);

    Vehicle findByVehicleNumberAndDeleted(String vehicleNumber, boolean isDeleted);
}
