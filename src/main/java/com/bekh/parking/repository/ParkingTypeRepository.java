package com.bekh.parking.repository;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.ParkingType;
import com.bekh.parking.model.User;
import com.bekh.parking.model.VehicleType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingTypeRepository extends CrudRepository<ParkingType, Long> {

    Optional<ParkingType> findByIdAndDeleted(Long id, boolean isDeleted);

    List<ParkingType> findAllByDeleted(boolean deleted);

    ParkingType findByTypeAndDeleted(VehicleType type, boolean isDeleted);
}
