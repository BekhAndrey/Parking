package com.bekh.parking.controller;

import com.bekh.parking.model.*;
import com.bekh.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ParkingTypeService parkingTypeService;

    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderHistoryService orderHistoryService;

    @PostMapping("/add")
    public String createParkingLot(Authentication authentication, @Valid ParkingLot parkingLot,
                                   Errors errors, String carNumber, Model model) {
        if(parkingLot.getEnterDate()==null){
            errors.rejectValue("enterDate", "null", "Enter date cannot be empty.");
            model.addAttribute("cars",
                    vehicleService.findAllByOwnerId(userService.findUserByEmail(authentication.getName()).getId()));
            return "order/mainpage";
        }
        if(parkingLot.getExitDate()==null){
            errors.rejectValue("exitDate", "null", "Exit date cannot be empty.");
            model.addAttribute("cars",
                    vehicleService.findAllByOwnerId(userService.findUserByEmail(authentication.getName()).getId()));
            return "order/mainpage";
        }
        double multiplier = 0;
        Vehicle vehicleToPark = vehicleService.findByVehicleNumber(carNumber);
        if (parkingLot.getEnterDate().isAfter(parkingLot.getExitDate())) {
            errors.rejectValue("enterDate", "isAfter", "Enter date cannot be after exit date.");
            model.addAttribute("cars",
                    vehicleService.findAllByOwnerId(userService.findUserByEmail(authentication.getName()).getId()));
            return "order/mainpage";
        }
        for (LocalDate date = parkingLot.getEnterDate();
             date.isBefore(parkingLot.getExitDate().plusDays(1));
             date = date.plusDays(1)) {
            if (parkingLotService.findAllCurrentlyParked(date)
                    .size() > parkingTypeService.findByType(vehicleToPark.getVehicleType()).getLotsAmount()) {
                errors.rejectValue("enterDate", "noLots", "No parking lots available for this period.");
            }
            if (parkingLotService.findCurrentlyParkedByVehicleNumber(date, carNumber).size() > 0) {
                errors.rejectValue("enterDate", "exists",
                        "There is another booking for this car during this period.");
            }
            if (errors.hasErrors()) {
                model.addAttribute("cars",
                        vehicleService.findAllByOwnerId(userService.findUserByEmail(authentication.getName()).getId()));
                return "order/mainpage";
            }
        }
        long parkingDuration = DAYS.between(parkingLot.getEnterDate(), parkingLot.getExitDate());
        switch (vehicleToPark.getVehicleType()) {
            case CAR:
                multiplier = 1.05;
                break;
            case VAN:
                multiplier = 1.1;
                break;
            case MOTORCYCLE:
                multiplier = 1;
                break;
            default:
                break;
        }
        parkingLot.setVehicle(vehicleToPark);
        String price = (Math.round((parkingDuration * 5 * multiplier) * 100) / 100) + " BYN";
        model.addAttribute("price", price);
        model.addAttribute("parkingLot", parkingLot);
        return "order/confirmbooking";
    }

    @PostMapping("/confirm")
    public String confirmOrder(Authentication authentication, ParkingLot parkingLot,
                               String price, String vehicleNumber) {
        Vehicle vehicleToPark = vehicleService.findByVehicleNumber(vehicleNumber);
        parkingLot.setVehicle(vehicleToPark);
        parkingLot.setParkingType(parkingTypeService.findByType(vehicleToPark.getVehicleType()));
        parkingLotService.save(parkingLot);
        Order order = new Order();
        order.setParkingLot(parkingLot);
        order.setUser(userService.findUserByEmail(authentication.getName()));
        order.setStatus(Status.RESERVED);
        order.setPrice(price);
        orderService.save(order);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setUser(userService.findUserByEmail(authentication.getName()));
        orderHistory.setParkingLotId(parkingLot.getId());
        orderHistory.setEnterDate(parkingLot.getEnterDate());
        orderHistory.setExitDate(parkingLot.getExitDate());
        orderHistory.setVehicleNumber(vehicleToPark.getVehicleNumber());
        orderHistory.setVehicleType(vehicleToPark.getVehicleType());
        orderHistory.setPrice(price);
        orderHistory.setStatus(Status.RESERVED);
        orderHistoryService.save(orderHistory);
        return "redirect:/home";
    }

    @GetMapping()
    public String ordersPage(Authentication authentication, Model model) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderService.updateUserOrdersStatus(loggedUser);
        model.addAttribute("orders", orderService.findByUser(loggedUser));
        return "order/orders";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable("id") Long id, Model model, Authentication authentication) {
        if (!orderService.findById(id).getUser().getEmail().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        model.addAttribute("order", orderService.findById(id));
        return "order/orderpage";
    }

    @GetMapping("/{id}/edit")
    public String editOrderForm(@PathVariable("id") Long id, Model model, Authentication authentication) {
        if (!orderService.findById(id).getUser().getEmail().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!orderService.findById(id).getParkingLot().getEnterDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("car", orderService.findById(id).getParkingLot().getVehicle());
        model.addAttribute("parkingLot", parkingLotService.findById(orderService.findById(id).getParkingLot().getId()));
        return "order/editorder";
    }

    @PostMapping("/{id}/edit/confirm")
    public String confirmEdit(@PathVariable("id") Long id, @Valid ParkingLot parkingLot,
                              Errors errors, String carNumber, Model model) {
        double multiplier = 0;
        Vehicle vehicleToPark = vehicleService.findByVehicleNumber(carNumber);
        if (parkingLot.getEnterDate().isAfter(parkingLot.getExitDate())) {
            errors.rejectValue("enterDate", "isAfter", "Enter date cannot be after exit date.");
            model.addAttribute("car",
                    vehicleToPark);
            return "order/editorder";
        }
        for (LocalDate date = parkingLot.getEnterDate(); date.isBefore(parkingLot.getExitDate().plusDays(1)); date = date.plusDays(1)) {
            List<ParkingLot> lots = parkingLotService.findCurrentlyParkedByVehicleNumber(date, carNumber);
            if (lots.size() > 0 && !lots.get(0).getId().equals(parkingLot.getId())) {
                errors.rejectValue("enterDate", "exists", "There is another booking for this car during this period.");
                model.addAttribute("car",
                        vehicleToPark);
                return "order/editorder";
            }
            if (parkingLotService.findAllCurrentlyParked(date)
                    .size() > parkingTypeService.findByType(vehicleToPark.getVehicleType()).getLotsAmount()) {
                errors.rejectValue("enterDate", "noLots", "No parking lots available for this period.");
                model.addAttribute("car",
                        vehicleToPark);
                return "order/editorder";
            }
        }
        long parkingDuration = DAYS.between(parkingLot.getEnterDate(), parkingLot.getExitDate());
        switch (vehicleToPark.getVehicleType()) {
            case CAR:
                multiplier = 1.05;
                break;
            case VAN:
                multiplier = 1.1;
                break;
            case MOTORCYCLE:
                multiplier = 1;
                break;
            default:
                break;
        }
        parkingLot.setVehicle(vehicleToPark);
        String price = (Math.round((parkingDuration * 5 * multiplier) * 100) / 100) + " BYN";
        model.addAttribute("price", price);
        model.addAttribute("parkingLot", parkingLot);
        return "order/confirmedit";
    }

    @PutMapping("/{id}/edit")
    public String editOrder(@PathVariable("id") Long id, ParkingLot parkingLot, String price) {
        ParkingLot lotToUpdate = parkingLotService.findById(parkingLot.getId());
        lotToUpdate.setEnterDate(parkingLot.getEnterDate());
        lotToUpdate.setExitDate(parkingLot.getExitDate());
        parkingLotService.save(lotToUpdate);
        Order orderToUpdate = orderService.findByParkingLot(parkingLotService.findById(parkingLot.getId()));
        orderToUpdate.setPrice(price);
        orderService.save(orderToUpdate);
        OrderHistory orderHistory = orderHistoryService.findByParkingLotId(lotToUpdate.getId());
        orderHistory.setEnterDate(lotToUpdate.getEnterDate());
        orderHistory.setExitDate(lotToUpdate.getExitDate());
        orderHistory.setPrice(price);
        orderHistoryService.save(orderHistory);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/cancel")
    public String confirmCancelOrder(@PathVariable("id") Long id, Model model, Authentication authentication) {
        if (!orderService.findById(id).getUser().getEmail().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!orderService.findById(id).getParkingLot().getEnterDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        model.addAttribute("parkingLot", parkingLotService.findById(order.getParkingLot().getId()));
        return "order/confirmcancel";
    }

    @DeleteMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable("id") Long id, ParkingLot parkingLot) {
        Order order = orderService.findByParkingLot(parkingLot);
        orderService.delete(order);
        parkingLotService.delete(parkingLotService.findById(parkingLot.getId()));
        OrderHistory orderHistory = orderHistoryService.findByParkingLotId(parkingLot.getId());
        orderHistory.setStatus(Status.CANCELED);
        orderHistoryService.save(orderHistory);
        return "redirect:/orders";
    }
}
