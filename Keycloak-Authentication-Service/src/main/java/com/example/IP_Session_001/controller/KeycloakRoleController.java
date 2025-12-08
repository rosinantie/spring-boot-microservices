package com.example.IP_Session_001.controller;

import com.example.IP_Session_001.service.KeycloakRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keycloak-roles")
@RequiredArgsConstructor
@Slf4j
public class KeycloakRoleController {

    private final KeycloakRoleService keycloakRoleService;

    @PostMapping("/add")
    public ResponseEntity<?> addRole(@RequestParam String roleName) {
        try {
            RoleRepresentation newRole = keycloakRoleService.addRole(roleName);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
        } catch (Exception e) {
            log.error("Error adding Keycloak role: {}", roleName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create role in Keycloak");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            List<RoleRepresentation> roles = keycloakRoleService.getAllRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            log.error("Error fetching Keycloak roles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch Keycloak roles");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testrole() {
        return ResponseEntity.ok("Keycloak Roles Working");
    }
}
