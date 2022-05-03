package com.bekh.parking.controller;

import com.bekh.parking.model.*;
import com.bekh.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderHistoryService orderHistoryService;

    @Autowired
    private ParkingTypeService parkingTypeService;

    @GetMapping()
    public String adminPanel() {
        return "redirect:/admin/users";
    }

    @GetMapping("/users")
    public String adminPanelUsers(Model model) {
        model.addAttribute("users", userService.findAllByRole(UserRole.ROLE_USER));
        model.addAttribute("vehicles", vehicleService.findAll());
        List<ParkingLot> parkingLotList = parkingLotService.findAllCurrentlyParked(LocalDate.now().plusDays(1));
        List<ParkingType> types = parkingTypeService.findAll();
        Map<String, Integer> totalLots = new HashMap<>();
        Map<String, Integer> availableLots = new HashMap<>();
        for(ParkingType parkingType: types){
            Long amount = parkingLotList.stream().filter(lot->lot.getParkingType().getType().equals(parkingType.getType())).count();
            totalLots.put(parkingType.getType().name(), parkingType.getLotsAmount());
            availableLots.put(parkingType.getType().name(), parkingType.getLotsAmount() - amount.intValue());
        }
        model.addAttribute("total", totalLots);
        model.addAttribute("available", availableLots);
        return "admin/adminpanel";
    }

    @GetMapping("/users/{id}/delete")
    public String confirmDeleteUser(@PathVariable("id") Long id) {
        return "account/confirmdeleteuser";
    }

    @DeleteMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        for (OrderHistory orderHistory : user.getHistory()) {
            orderHistoryService.delete(orderHistory);
        }
        for (Order order : orderService.findByUser(user)) {
            orderService.delete(order);
        }
        for (Vehicle vehicle : vehicleService.findAllByOwnerId(user.getId())) {
            for (ParkingLot parkingLot : parkingLotService.findAllByVehicle(vehicle)) {
                parkingLotService.delete(parkingLot);
            }
            vehicleService.delete(vehicle);
        }
        try{
            userService.delete(user);
        } catch (Throwable e){
            return "redirect:/admin/users";
        }
        return "redirect:/admin";
    }

    @GetMapping("/vehicles/{id}/delete")
    public String confirmDeleteVehicle(@PathVariable("id") Long id) {
        return "vehicle/confirmdeletevehicle";
    }

    @DeleteMapping("/vehicles/{id}/delete")
    public String deleteVehicle(@PathVariable("id") Long id) {
        Vehicle vehicle = vehicleService.findById(id);
        for (ParkingLot parkingLot : parkingLotService.findAllByVehicle(vehicle)) {
            orderService.delete(orderService.findByParkingLot(parkingLot));
            parkingLotService.delete(parkingLot);
        }
        vehicleService.delete(vehicle);
        return "redirect:/admin";
    }

    @GetMapping("/orders")
    public String adminPanelOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin/adminpanelorders";
    }

    @GetMapping("/orders/{id}/delete")
    public String confirmDeleteOrder(@PathVariable("id") Long id) {
        return "order/confirmdeleteorder";
    }

    @DeleteMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable("id") Long id) {
        Order order = orderService.findById(id);
        orderService.delete(order);
        orderHistoryService.delete(orderHistoryService.findByParkingLotId(order.getParkingLot().getId()));
        parkingLotService.delete(order.getParkingLot());
        return "redirect:/admin/orders";
    }

    @GetMapping("/history")
    public String adminPanelHistory(Model model) {
        model.addAttribute("histories", orderHistoryService.findAll());
        return "admin/adminpanelhistories";
    }

    @GetMapping("/history/{id}/delete")
    public String confirmDeleteHistory(@PathVariable("id") Long id) {
        return "history/confirmdeletehistory";
    }

    @DeleteMapping("/history/{id}/delete")
    public String deleteHistory(@PathVariable("id") Long id) {
        orderHistoryService.delete(orderHistoryService.findById(id));
        return "redirect:/admin/history";
    }
}
