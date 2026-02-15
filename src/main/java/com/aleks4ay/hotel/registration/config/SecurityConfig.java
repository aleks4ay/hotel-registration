package com.aleks4ay.hotel.registration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String LOGIN_PATH = "/login";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ClientRegistrationRepository clientRegistrationRepository,
                                                   KeycloakAuthenticationProvider keycloakAuthenticationProvider) throws Exception {
        http
                .authenticationProvider(keycloakAuthenticationProvider)
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/admin/**").hasRole("hotel_provider_admin")
                                .requestMatchers("/profile/**").hasRole("hotel_data_writer")
                                .requestMatchers("/", "/css/**", LOGIN_PATH, "/logout", "/realms/**", "/register", "/adminnn/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(LOGIN_PATH)
                        .loginProcessingUrl(LOGIN_PATH)
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(
                                oidcLogoutSuccessHandler(clientRegistrationRepository)
                        )
                );

        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {

        OidcClientInitiatedLogoutSuccessHandler handler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        handler.setPostLogoutRedirectUri("{baseUrl}/");
        return handler;
    }

}
