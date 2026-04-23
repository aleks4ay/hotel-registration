package com.aleks4ay.hotel.registration.income;

import com.aleks4ay.hotel.registration.util.HtmlPage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final String CURRENT_URI = "currentUri";

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        return home(model, request);
    }

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute(CURRENT_URI, request.getRequestURI());
        return HtmlPage.HOME.getPageName();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        model.addAttribute(CURRENT_URI, request.getRequestURI());
        return HtmlPage.DASHBOARD.getPageName();
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication, HttpServletRequest request) {
        model.addAttribute(CURRENT_URI, request.getRequestURI());
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());

        return HtmlPage.PROFILE.getPageName();
    }

    @GetMapping("/admin")
    public String admin(Model model, Authentication authentication, HttpServletRequest request) {
        model.addAttribute(CURRENT_URI, request.getRequestURI());
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());

        return HtmlPage.ADMIN.getPageName();
    }
}
