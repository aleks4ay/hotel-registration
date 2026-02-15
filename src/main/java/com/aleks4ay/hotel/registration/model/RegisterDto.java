package com.aleks4ay.hotel.registration.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

    @NotBlank(message = "Username обязателен")
    private String username;

    @NotBlank(message = "FirstName обязателен")
    private String firstName;

    @NotBlank(message = "LastName обязателен")
    private String lastName;

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @Size(min = 4, message = "Минимум 4 символа")
    private String password;

    @NotBlank(message = "Подтвердите пароль")
    private String confirmPassword;
}

