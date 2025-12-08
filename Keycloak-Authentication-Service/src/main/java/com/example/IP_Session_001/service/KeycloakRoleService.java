package com.example.IP_Session_001.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakRoleService {

    private final Keycloak keycloak;

    public KeycloakRoleService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Value("${keycloak.realm}")
    private String realm;

    // Add a new role
    public RoleRepresentation addRole(String roleName) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);

        // Call create() without capturing response
        keycloak.realm(realm).roles().create(role); // void call

        // Since we don't have Response, just fetch the role directly
        return keycloak.realm(realm).roles().get(roleName).toRepresentation();
    }

    // Get all roles
    public List<RoleRepresentation> getAllRoles() {
        return keycloak.realm(realm).roles().list();
    }
}
