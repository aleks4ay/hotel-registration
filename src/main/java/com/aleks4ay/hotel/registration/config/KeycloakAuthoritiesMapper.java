package com.aleks4ay.hotel.registration.config;

import com.aleks4ay.hotel.registration.config.properties.KeycloakClientProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;

@Configuration
@RequiredArgsConstructor
public class KeycloakAuthoritiesMapper {

    private final KeycloakClientProperties keycloakClientProperties;

    @Bean
    GrantedAuthoritiesMapper realmRoleMapper() {
        return authorities -> {
            Set<GrantedAuthority> mapped = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority oidc) {
                    mapped.addAll(extractRolesFromToken(oidc.getIdToken()));
                }
            });
            return mapped;
        };
    }

    @Bean
    public Keycloak keycloakAdmin() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakClientProperties.getUrl())
                .realm(keycloakClientProperties.getRealm())
                .clientId(keycloakClientProperties.getId())
                .clientSecret(keycloakClientProperties.getSecret())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public static Set<GrantedAuthority> extractRolesFromToken(ClaimAccessor token) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        Object realmAccess = token.getClaims().get("realm_access");
        if (nonNull(realmAccess) && realmAccess instanceof Map<?, ?> realmAccessMap) {
            var roleObject = realmAccessMap.get("roles");
            if (nonNull(roleObject) && roleObject instanceof List<?> roles) {
                for (var role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }
        }
        return authorities;
    }
}
