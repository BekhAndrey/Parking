package com.bekh.parking.repository;

import com.bekh.parking.model.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Optional<Order> findByIdAndDeleted(Long id, boolean isDeleted);

    List<Order> findAllByDeleted(boolean deleted);

    List<Order> findByUserAndDeleted(User user, boolean isDeleted);

    List<Order> findByStatusAndDeleted(Status status, boolean isDeleted);

    Order findByParkingLotAndDeleted(ParkingLot parkingLot, boolean isDeleted);
}
