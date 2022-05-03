package com.bekh.parking.service;

import com.bekh.parking.model.OrderHistory;
import com.bekh.parking.model.Status;
import com.bekh.parking.model.User;
import com.bekh.parking.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service("orderHistoryService")
@RequiredArgsConstructor
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    public List<OrderHistory> findAll() {
        return orderHistoryRepository.findAllByDeleted(false);
    }

    public OrderHistory findById(Long id) {
        return orderHistoryRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the resource"));
    }

    public void save(OrderHistory orderHistory) {
        orderHistoryRepository.save(orderHistory);
    }

    public void delete(OrderHistory orderHistory) {
        orderHistory.setDeleted(true);
        orderHistoryRepository.save(orderHistory);
    }

    public OrderHistory findByParkingLotId(Long id) {
        return orderHistoryRepository.findByParkingLotIdAndDeleted(id, false).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the resource"));
    }

    public List<OrderHistory> findAllByUser(User user) {
        return orderHistoryRepository.findAllByUserAndDeleted(user, Sort.by("enterDate").descending(), false);
    }

    public void updateUserHistoryStatus(User user) {
        List<OrderHistory> orders = orderHistoryRepository.findAllByUserAndDeleted(user, Sort.by("enterDate").descending(), false);
        for (OrderHistory order : orders) {
            if (order.getStatus().equals(Status.RESERVED) &&
                    (order.getEnterDate().equals(LocalDate.now()) || order.getEnterDate().isBefore(LocalDate.now()))) {
                order.setStatus(Status.ONGOING);
            }
            if (order.getStatus().equals(Status.ONGOING) && (order.getExitDate().equals(LocalDate.now()) || order.getExitDate().isBefore(LocalDate.now()))) {
                order.setStatus(Status.COMPLETED);
            }
            orderHistoryRepository.save(order);
        }
    }

    public List<OrderHistory> findAllByUserAndStatus(User user, Status status) {
        return orderHistoryRepository.findAllByUserAndStatusAndDeleted(user, status, Sort.by("enterDate").descending(), false);
    }
}
