package com.bekh.parking.repository;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.OrderHistory;
import com.bekh.parking.model.Status;
import com.bekh.parking.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderHistoryRepository extends CrudRepository<OrderHistory, Long>, PagingAndSortingRepository<OrderHistory, Long> {
    OrderHistory findByParkingLotId(Long id);

    List<OrderHistory> findAllByUser(User user, Sort sort);

    List<OrderHistory> findAllByUserAndStatus(User user, Status status, Sort sort);
}
