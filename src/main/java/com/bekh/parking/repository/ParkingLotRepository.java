package com.bekh.parking.repository;

import com.bekh.parking.model.ParkingLot;
import com.bekh.parking.model.User;
import org.springframework.data.repository.CrudRepository;

public interface ParkingLotRepository extends CrudRepository<ParkingLot, Long> {
}
