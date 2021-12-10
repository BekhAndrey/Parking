package com.bekh.parking.controller;


import com.bekh.parking.model.ResetForm;
import com.bekh.parking.model.User;
import com.bekh.parking.service.EmailService;
import com.bekh.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.validation.Valid;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private JedisPool jedisPool;

    private static final String CONFIRM_EMAIL_SUBJECT = "Confirm your email address";
    private static final String CONFIRM_EMAIL_MESSAGE = "Use this code to confirm your email:\n";
    private static final String RESET_PASSWORD_SUBJECT = "Reset your password";
    private static final String RESET_PASSWORD_MESSAGE = "Use this code to reset your password:\n";
    private static final int DAY = 86400;

    @GetMapping("/")
    public String home() {
        return "credentials/login";
    }


    @GetMapping("/registration")
    public String sighUpPage(Model model) {
        model.addAttribute("user", new User());
        return "credentials/signup";
    }

    @PostMapping("/registration")
    public String processSignUp(@Valid User user, Errors errors) {
        if (userService.findUserByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "email.notUnique", "This email is already in use.");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "confirmPassword.dontMatch", "Passwords dont match.");
        }
        if (errors.hasErrors()) {
            return "credentials/signup";
        }
        userService.create(user);
        String code = UUID.randomUUID().toString();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(code, user.getEmail());
            jedis.expire(code, DAY);
        }
        emailService.sendSimpleMessage(user.getEmail(), CONFIRM_EMAIL_SUBJECT, CONFIRM_EMAIL_MESSAGE + code);
        return "redirect:/registration/confirm-email";
    }

    @GetMapping("/registration/confirm-email")
    public String confirmEmailForm() {
        return "credentials/confirmemail";
    }

    @PostMapping("/registration/confirm-email")
    public String confirmEmail(String code) {
        String email;
        try (Jedis jedis = jedisPool.getResource()) {
            if(jedis.get(code)==null){
                return "redirect:/registration/confirm-email?error";
            }
            email = jedis.get(code);
        }
        User userToApprove = userService.findUserByEmail(email);
        userToApprove.setApproved(true);
        userService.update(userToApprove);
        return "credentials/login";
    }

    @GetMapping("auth/forgot-password")
    public String getCodeForm() {
        return "credentials/getresetcode";
    }

    @PostMapping("auth/forgot-password")
    public String getCode(String email) {
        if (userService.findUserByEmail(email) == null) {
            return "redirect:/auth/forgot-password?error";
        }
        String code = UUID.randomUUID().toString();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(code, email);
            jedis.expire(code, DAY);
        }
        emailService.sendSimpleMessage(email, RESET_PASSWORD_SUBJECT, RESET_PASSWORD_MESSAGE + code);
        return "redirect:/auth/reset";
    }

    @GetMapping("auth/reset")
    public String resetPasswordForm(Model model) {
        model.addAttribute("resetForm", new ResetForm());
        return "credentials/resetpassword";
    }

    @PostMapping("auth/reset")
    public String resetPassword(@Valid ResetForm resetForm, Errors errors) {
        if (!resetForm.getNewPassword().equals(resetForm.getConfirmNewPassword())) {
            errors.rejectValue("confirmNewPassword", "passwords.dontMatch", "Passwords dont match.");
        }
        String email;
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.get(resetForm.getCode()) == null) {
                errors.rejectValue("code", "code.notFound", "This code does not exist.");
            }
            email = jedis.get(resetForm.getCode());
            jedis.del(resetForm.getCode());
        }
        if (errors.hasErrors()) {
            return "credentials/resetpassword";
        }
        User userToUpdate = userService.findUserByEmail(email);
        userToUpdate.setPassword(resetForm.getNewPassword());
        userService.updatePassword(userToUpdate);
        return "credentials/login";
    }
}
