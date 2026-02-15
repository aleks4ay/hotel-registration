package com.aleks4ay.hotel.registration.config;

import com.aleks4ay.hotel.registration.config.properties.KeycloakClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class KeycloakAuthenticationProvider implements AuthenticationProvider {

    private final RestTemplate restTemplate = new RestTemplate();
    private final KeycloakClientProperties properties;

    @Override
    public Authentication authenticate(Authentication authentication) {
        var baseUrl = properties.getUrl() + "/realms/" + properties.getRealm() + "/protocol/openid-connect";

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", properties.getId());
        body.add("client_secret", properties.getSecret());
        body.add("username", username);
        body.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        var response = restTemplate.postForEntity(baseUrl + "/token", request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(baseUrl + "/certs").build();

            Set<GrantedAuthority> roles = Optional.of(response.getBody())
                    .map(it -> it.get("access_token"))
                    .filter(String.class::isInstance)
                    .map(String::valueOf)
                    .map(jwtDecoder::decode)
                    .map(KeycloakAuthoritiesMapper::extractRolesFromToken)
                    .orElse(Set.of());

            return new UsernamePasswordAuthenticationToken(username, null, roles);
        }
        throw new BadCredentialsException("Invalid credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
