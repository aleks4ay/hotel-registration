package com.aleks4ay.hotel.registration.income;

import com.aleks4ay.hotel.registration.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @GetMapping("/adminnn/{name}")
    public String setAdmin(@PathVariable String name) {
        registerService.assignRealmRole(name);
        return name;
    }
}
