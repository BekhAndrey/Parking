package com.bekh.parking.repository;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.ParkingLot;
import com.bekh.parking.model.Status;
import com.bekh.parking.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findByStatus(Status status);

    Order findByParkingLot(ParkingLot parkingLot);
}
