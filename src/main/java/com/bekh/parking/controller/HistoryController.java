package com.bekh.parking.controller;

import com.bekh.parking.model.Status;
import com.bekh.parking.model.User;
import com.bekh.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderHistoryService orderHistoryService;

    @GetMapping()
    public String history(Authentication authentication, Model model) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUser(loggedUser));
        return "history/history";
    }

    @GetMapping("/reserved")
    public String historyReserved(Authentication authentication, Model model) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUserAndStatus(loggedUser, Status.RESERVED));
        model.addAttribute("status", "Reserved");
        return "history/history";
    }

    @GetMapping("/ongoing")
    public String historyOngoing(Authentication authentication, Model model) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUserAndStatus(loggedUser, Status.ONGOING));
        model.addAttribute("status", "Ongoing");
        return "history/history";
    }

    @GetMapping("/completed")
    public String historyCompleted(Authentication authentication, Model model) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUserAndStatus(loggedUser, Status.COMPLETED));
        model.addAttribute("status", "Completed");
        return "history/history";
    }

    @GetMapping("/canceled")
    public String historyCanceled(Authentication authentication, Model model) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUserAndStatus(loggedUser, Status.CANCELED));
        model.addAttribute("status", "Canceled");
        return "history/history";
    }
}
