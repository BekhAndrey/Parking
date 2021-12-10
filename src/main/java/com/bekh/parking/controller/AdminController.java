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

    @GetMapping()
    public String adminPanel() {
        return "redirect:/admin/users";
    }

    @GetMapping("/users")
    public String adminPanelUsers(Model model) {
        model.addAttribute("users", userService.findAllByRole(UserRole.ROLE_USER));
        model.addAttribute("vehicles", vehicleService.findAll());
        return "adminpanel";
    }

    @GetMapping("/users/{id}/delete")
    public String confirmDeleteUser(@PathVariable("id") Long id) {
        return "confirmdeleteuser";
    }

    @DeleteMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        for (Order order : orderService.findByUser(user)) {
            orderService.delete(order);
        }
        for (Vehicle vehicle : user.getVehicles()) {
            for (ParkingLot parkingLot : parkingLotService.findAllByVehicle(vehicle)) {
                parkingLotService.delete(parkingLot);
            }
            vehicleService.delete(vehicle);
        }
        for (OrderHistory orderHistory : user.getHistory()) {
            orderHistoryService.delete(orderHistory);
        }
        userService.delete(user);
        return "redirect:/admin";
    }

    @GetMapping("/vehicles/{id}/delete")
    public String confirmDeleteVehicle(@PathVariable("id") Long id) {
        return "confirmdeletevehicle";
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
        return "adminpanelorders";
    }

    @GetMapping("/orders/{id}/delete")
    public String confirmDeleteOrder(@PathVariable("id") Long id) {
        return "confirmdeleteorder";
    }

    @DeleteMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable("id") Long id) {
        Order order = orderService.findById(id);
        orderService.delete(order);
        parkingLotService.delete(order.getParkingLot());
        return "redirect:/admin/orders";
    }

    @GetMapping("/history")
    public String adminPanelHistory(Model model) {
        model.addAttribute("histories", orderHistoryService.findAll());
        return "adminpanelhistories";
    }

    @GetMapping("/history/{id}/delete")
    public String confirmDeleteHistory(@PathVariable("id") Long id) {
        return "confirmdeletehistory";
    }

    @DeleteMapping("/history/{id}/delete")
    public String deleteHistory(@PathVariable("id") Long id) {
        orderHistoryService.delete(orderHistoryService.findById(id));
        return "redirect:/admin/history";
    }
}
