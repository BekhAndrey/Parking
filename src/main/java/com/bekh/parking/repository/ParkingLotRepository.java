package com.bekh.parking.repository;

import com.bekh.parking.model.ParkingLot;
import com.bekh.parking.model.User;
import com.bekh.parking.model.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParkingLotRepository extends CrudRepository<ParkingLot, Long> {

    @Query("select p from ParkingLot p where :date between p.enterDate and p.exitDate")
    List<ParkingLot> findAllCurrentlyParked(@Param("date") LocalDate date);

    @Query("select p from ParkingLot p where :date between p.enterDate and p.exitDate and p.vehicle.vehicleNumber = :vehicleNumber")
    List<ParkingLot> findCurrentlyParkedByVehicleNumber(@Param("date") LocalDate date,
                                                                   @Param("vehicleNumber") String vehicleNumber);

    List<ParkingLot> findAllByVehicle(Vehicle vehicle);
}
