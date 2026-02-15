package com.aleks4ay.hotel.registration.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.client")
public class KeycloakClientProperties {

    private String realm;
    private String url;
    private String secret;
    private String id;
}
