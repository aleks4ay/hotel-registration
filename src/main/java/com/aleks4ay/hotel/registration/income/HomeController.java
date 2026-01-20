package com.aleks4ay.hotel.registration.income;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    @SuppressWarnings("unused")
    public String home() {
        return "index";
    }

    @GetMapping("/profile")
    @SuppressWarnings("unused")
    public String profile(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", auth.getAuthorities());
        return "profile";
    }
}
