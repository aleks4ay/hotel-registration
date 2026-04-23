package com.aleks4ay.hotel.registration.config;

import com.aleks4ay.hotel.registration.util.HtmlPage;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ClientRegistrationRepository clientRegistrationRepository,
                                                   KeycloakAuthenticationProvider keycloakAuthenticationProvider) throws Exception {
        http
                .authenticationProvider(keycloakAuthenticationProvider)
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers( HtmlPage.ADMIN.asAllPaths()).hasRole("hotel_provider_admin")
                                .requestMatchers(HtmlPage.PROFILE.asAllPaths()).hasRole("hotel_data_writer")
                                .requestMatchers(HtmlPage.DEFAULT.asPath(), HtmlPage.CSS.asAllPaths(), HtmlPage.LOGIN.asPath(),
                                        HtmlPage.LOGOUT.asPath(), HtmlPage.REALMS.asAllPaths(), HtmlPage.HOME.asPath(),
                                        HtmlPage.REGISTER.asPath()).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(HtmlPage.LOGIN.asPath())
                        .loginProcessingUrl(HtmlPage.LOGIN.asPath())
                        .defaultSuccessUrl(HtmlPage.DEFAULT.asPath(), true)
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
