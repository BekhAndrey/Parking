package com.bekh.parking.repository;

import com.bekh.parking.model.Order;
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

    Optional<ParkingLot> findByIdAndDeleted(Long id, boolean isDeleted);

    List<ParkingLot> findAllByDeleted(boolean deleted);

    @Query("select p from ParkingLot p where :date between p.enterDate and p.exitDate and p.deleted = false")
    List<ParkingLot> findAllCurrentlyParked(@Param("date") LocalDate date);

    @Query("select p from ParkingLot p where :date between p.enterDate and p.exitDate and p.vehicle.vehicleNumber = :vehicleNumber and p.deleted = false")
    List<ParkingLot> findCurrentlyParkedByVehicleNumber(@Param("date") LocalDate date,
                                                                   @Param("vehicleNumber") String vehicleNumber);

    List<ParkingLot> findAllByVehicleAndDeleted(Vehicle vehicle, boolean isDeleted);
}
