package com.bekh.parking.controller;

import com.bekh.parking.model.*;
import com.bekh.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;


@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/home")
    public String homePage(Authentication authentication, Model model) {
        model.addAttribute("cars",
                vehicleService.findAllByOwnerId(userService.findUserByEmail(authentication.getName()).getId()));
        model.addAttribute("parkingLot", new ParkingLot());
        return "order/mainpage";
    }

    @GetMapping("/account")
    public String accountPage(Authentication authentication, Model model) {
        model.addAttribute("currentUser", userService.findUserByEmail(authentication.getName()));
        model.addAttribute("vehicle", new Vehicle());
        return "account/account";
    }

    @GetMapping("/vehicles/add")
    public String addVehicleForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "vehicle/addvehicle";
    }

    @PostMapping("/vehicles/add")
    public String addVehicle(@Valid Vehicle vehicle, Errors errors, Authentication authentication, Locale locale) {
        if (vehicleService.findByVehicleNumber(vehicle.getVehicleNumber()) != null) {
            errors.rejectValue("vehicleNumber", "vehicleNumber.notUnique",
                    messageSource.getMessage("vehicle.exists", new Object[]{}, locale));
        }
        if (errors.hasErrors()) {
            return "vehicle/addvehicle";
        }
        vehicle.setOwner(userService.findUserByEmail(authentication.getName()));
        vehicleService.save(vehicle);
        return "redirect:/account";
    }
}
