package com.bekh.parking.service;

import com.bekh.parking.model.Order;
import com.bekh.parking.model.ParkingLot;
import com.bekh.parking.model.Status;
import com.bekh.parking.model.User;
import com.bekh.parking.repository.OrderRepository;
import com.bekh.parking.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service("orderService")
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public List<Order> findAll() {
        return orderRepository.findAllByDeleted(false);
    }

    public Order findById(Long id) {
        return orderRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the resource"));
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public void delete(Order order) {
        order.setDeleted(true);
        orderRepository.save(order);
    }

    public List<Order> findByUser(User user) {
        return orderRepository.findByUserAndDeleted(user, false);
    }

    public List<Order> findByStatus(Status status) {
        return orderRepository.findByStatusAndDeleted(status, false);
    }

    public Order findByParkingLot(ParkingLot parkingLot) {
        return orderRepository.findByParkingLotAndDeleted(parkingLot, false);
    }

    public void updateUserOrdersStatus(User user) {
        List<Order> orders = orderRepository.findByUserAndDeleted(user, false);
        for (Order order : orders) {
            if (order.getParkingLot().getEnterDate().equals(LocalDate.now())
                    || order.getParkingLot().getEnterDate().isBefore(LocalDate.now())) {
                order.setStatus(Status.ONGOING);
                orderRepository.save(order);
            }
            if(order.getParkingLot().getExitDate().equals(LocalDate.now())
                    || order.getParkingLot().getExitDate().isBefore(LocalDate.now())){
                orderRepository.delete(order);
                parkingLotRepository.delete(order.getParkingLot());
            }
        }
    }
}
