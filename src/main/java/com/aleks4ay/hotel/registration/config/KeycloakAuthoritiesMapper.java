package com.aleks4ay.hotel.registration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class KeycloakAuthoritiesMapper {

    @Bean
    GrantedAuthoritiesMapper realmRoleMapper() {
        return authorities -> {
            Set<GrantedAuthority> mapped = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority oidc) {
                    Map<String, Object> claims =
                            oidc.getIdToken().getClaims();

                    Map<String, Object> realmAccess =
                            (Map<String, Object>) claims.get("realm_access");

                    if (realmAccess != null) {
                        List<String> roles =
                                (List<String>) realmAccess.get("roles");

                        roles.forEach(role ->
                                mapped.add(
                                        new SimpleGrantedAuthority("ROLE_" + role)
                                )
                        );
                    }
                }
            });
            return mapped;
        };
    }
}
