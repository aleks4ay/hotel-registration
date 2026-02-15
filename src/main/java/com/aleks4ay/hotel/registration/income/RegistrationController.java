package com.aleks4ay.hotel.registration.income;

import com.aleks4ay.hotel.registration.model.RegisterDto;
import com.aleks4ay.hotel.registration.service.RegistrationService;
import com.aleks4ay.hotel.registration.util.HtmlPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Validated
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return HtmlPage.REGISTER.getPageName();
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDto") RegisterDto dto, BindingResult bindingResult, Model model) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Пароли не совпадают");
        }

        if (bindingResult.hasErrors()) {
            return HtmlPage.REGISTER.getPageName();
        }

        try {
            registrationService.registerUser(dto);
        } catch (Exception e) {
            model.addAttribute("registrationError", "Ошибка регистрации");
            return HtmlPage.REGISTER.getPageName();
        }

        return "redirect:/login";
    }
}
