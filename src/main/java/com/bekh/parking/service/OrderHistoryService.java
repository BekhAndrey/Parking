package com.bekh.parking.service;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.OrderHistory;
import com.bekh.parking.model.Status;
import com.bekh.parking.model.User;
import com.bekh.parking.repository.OrderHistoryRepository;
import com.bekh.parking.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service("orderHistoryService")
public class OrderHistoryService {
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    public List<OrderHistory> findAll() {
        return (List<OrderHistory>) orderHistoryRepository.findAll();
    }

    public OrderHistory findById(Long id) {
        return orderHistoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the resource"));
    }

    public void save(OrderHistory orderHistory) {
        orderHistoryRepository.save(orderHistory);
    }

    public void delete(OrderHistory orderHistory) {
        orderHistoryRepository.delete(orderHistory);
    }

    public OrderHistory findByParkingLotId(Long id) {
        return orderHistoryRepository.findByParkingLotId(id);
    }

    public List<OrderHistory> findAllByUser(User user) {
        return (List<OrderHistory>) orderHistoryRepository.findAllByUser(user, Sort.by("enterDate").descending());
    }

    public void updateUserHistoryStatus(User user){
        List<OrderHistory> orders = orderHistoryRepository.findAllByUser(user, Sort.by("enterDate").descending());
        for(OrderHistory order: orders){
            if(order.getStatus().equals(Status.RESERVED)&& order.getEnterDate().equals(LocalDate.now())){
                order.setStatus(Status.ONGOING);
                orderHistoryRepository.save(order);
                continue;
            }
            if(order.getStatus().equals(Status.ONGOING)&& order.getExitDate().equals(LocalDate.now())){
                order.setStatus(Status.COMPLETED);
                orderHistoryRepository.save(order);
            }
        }
    }

    public List<OrderHistory> findAllByUserAndStatus(User user, Status status) {
        return (List<OrderHistory>) orderHistoryRepository.findAllByUserAndStatus(user, status, Sort.by("enterDate").descending());
    }
}
