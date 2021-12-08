package com.bekh.parking.repository;

import com.bekh.parking.model.ParkingType;
import com.bekh.parking.model.User;
import com.bekh.parking.model.VehicleType;
import org.springframework.data.repository.CrudRepository;

public interface ParkingTypeRepository extends CrudRepository<ParkingType, Long> {
    ParkingType findByType(VehicleType type);
}
