package com.bekh.parking.controller;

import com.bekh.parking.model.*;
import com.bekh.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/home")
    public String homePage(Authentication authentication, Model model) {
        model.addAttribute("cars",
                vehicleService.findAllByOwnerId(userService.findUserByEmail(authentication.getName()).getId()));
        model.addAttribute("parkingLot", new ParkingLot());
        return "mainpage";
    }

    @GetMapping("/account")
    public String accountPage(Authentication authentication, Model model) {
        model.addAttribute("currentUser", userService.findUserByEmail(authentication.getName()));
        model.addAttribute("vehicle", new Vehicle());
        return "account";
    }

    @GetMapping("/vehicles/add")
    public String addVehicleForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "addvehicle";
    }

    @PostMapping("/vehicles/add")
    public String addVehicle(@Valid Vehicle vehicle, Errors errors, Authentication authentication) {
        if (vehicleService.findByVehicleNumber(vehicle.getVehicleNumber()) != null) {
            errors.rejectValue("vehicleNumber", "vehicleNumber.notUnique", "Vehicle with such number already exists.");
        }
        if (errors.hasErrors()) {
            return "addvehicle";
        }
        vehicle.setOwner(userService.findUserByEmail(authentication.getName()));
        vehicleService.save(vehicle);
        return "redirect:/account";
    }
}
