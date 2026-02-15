package com.aleks4ay.hotel.registration.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.new-user-roles")
public class RoleProperties {

    @NotNull
    public Set<String> users = new HashSet<>();

    @NotNull
    public Set<String> admins = new HashSet<>();
}
