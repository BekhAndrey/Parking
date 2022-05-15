package com.bekh.parking.controller;

import com.bekh.parking.model.Status;
import com.bekh.parking.model.User;
import com.bekh.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderHistoryService orderHistoryService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping()
    public String history(Authentication authentication, Model model) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUser(loggedUser));
        return "history/history";
    }

    @GetMapping("/reserved")
    public String historyReserved(Authentication authentication, Model model, Locale locale) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUserAndStatus(loggedUser, Status.RESERVED));
        model.addAttribute("status", messageSource.getMessage("status.reserved", new Object[]{}, locale));
        return "history/history";
    }

    @GetMapping("/ongoing")
    public String historyOngoing(Authentication authentication, Model model, Locale locale) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUserAndStatus(loggedUser, Status.ONGOING));
        model.addAttribute("status", messageSource.getMessage("status.ongoing", new Object[]{}, locale));
        return "history/history";
    }

    @GetMapping("/completed")
    public String historyCompleted(Authentication authentication, Model model, Locale locale) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUserAndStatus(loggedUser, Status.COMPLETED));
        model.addAttribute("status", messageSource.getMessage("status.completed", new Object[]{}, locale));
        return "history/history";
    }

    @GetMapping("/canceled")
    public String historyCanceled(Authentication authentication, Model model, Locale locale) {
        User loggedUser = userService.findUserByEmail(authentication.getName());
        orderHistoryService.updateUserHistoryStatus(loggedUser);
        model.addAttribute("ordersHistory", orderHistoryService.findAllByUserAndStatus(loggedUser, Status.CANCELED));
        model.addAttribute("status", messageSource.getMessage("status.canceled", new Object[]{}, locale));
        return "history/history";
    }
}
