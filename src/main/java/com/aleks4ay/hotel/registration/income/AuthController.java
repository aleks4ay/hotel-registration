package com.aleks4ay.hotel.registration.income;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    @SuppressWarnings("unused")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    @SuppressWarnings("unused")
    public String register() {
        return "register";
    }
}
