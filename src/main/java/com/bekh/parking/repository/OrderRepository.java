package com.bekh.parking.repository;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.User;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
