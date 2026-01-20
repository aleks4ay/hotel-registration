package com.aleks4ay.hotel.registration.service;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterService {

    public void assignRealmRole(String username) {

        String realm = "hotel-realm";

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:18080")
                .realm(realm)
                .clientId("hotel_registration_admin")
                .clientSecret("UIyn2JXR0J5x7nrQwTACGx11ICLQuZHr")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();

        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .search(username, true);

        var userId = users.get(0).getId();

        RoleRepresentation role = keycloak.realm(realm)
                .roles()
                .get("ADMIN")
                .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }

}
