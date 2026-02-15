package com.aleks4ay.hotel.registration.service;

import com.aleks4ay.hotel.registration.exception.UserCreateException;
import com.aleks4ay.hotel.registration.config.properties.KeycloakClientProperties;
import com.aleks4ay.hotel.registration.config.properties.RoleProperties;
import com.aleks4ay.hotel.registration.model.RegisterDto;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RoleProperties properties;
    private final KeycloakClientProperties keycloakClientProperties;
    private final Keycloak keycloakAdmin;

    public void registerUser(RegisterDto dto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(false);

        Response response = keycloakAdmin.realm(keycloakClientProperties.getRealm())
                .users()
                .create(user);

        if (response.getStatus() != 201) {
            throw new UserCreateException(response.getStatus());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);

        setPassword(dto, userId);
        setRoles(userId, properties.getAdmins());

        response.close();
    }

    private void setPassword(RegisterDto dto, String userId) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.getPassword());
        credential.setTemporary(false);

        keycloakAdmin.realm(keycloakClientProperties.getRealm())
                .users()
                .get(userId)
                .resetPassword(credential);
    }

    private void setRoles(String userId, Set<String> roleNames) {
        List<RoleRepresentation> newRoles = getKeycloakRolesByName(roleNames);

        if (!newRoles.isEmpty()) {
            keycloakAdmin.realm(keycloakClientProperties.getRealm())
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(newRoles);
        }
    }

    private List<RoleRepresentation> getKeycloakRolesByName(Set<String> roleNames) {
        List<RoleRepresentation> newRoles = new ArrayList<>();
        for (var newRole : roleNames) {
            RoleRepresentation keycloakRole = keycloakAdmin
                    .realm(keycloakClientProperties.getRealm())
                    .roles()
                    .get(newRole)
                    .toRepresentation();
            newRoles.add(keycloakRole);
        }
        return newRoles;
    }
}
