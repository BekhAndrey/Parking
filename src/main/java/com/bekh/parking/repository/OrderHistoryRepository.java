package com.bekh.parking.repository;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.OrderHistory;
import com.bekh.parking.model.Status;
import com.bekh.parking.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface OrderHistoryRepository extends CrudRepository<OrderHistory, Long>, PagingAndSortingRepository<OrderHistory, Long> {

    Optional<OrderHistory> findByIdAndDeleted(Long id, boolean isDeleted);

    List<OrderHistory> findAllByDeleted(boolean isDeleted);

    Optional<OrderHistory> findByParkingLotIdAndDeleted(Long id, boolean isDeleted);

    List<OrderHistory> findAllByUserAndDeleted(User user, Sort sort, boolean isDeleted);

    List<OrderHistory> findAllByUserAndStatusAndDeleted(User user, Status status, Sort sort, boolean isDeleted);
}
