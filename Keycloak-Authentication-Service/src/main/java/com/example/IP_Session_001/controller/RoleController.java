package com.example.IP_Session_001.controller;

import com.example.IP_Session_001.Enum.RoleEnum;
import com.example.IP_Session_001.entity.Role;
import com.example.IP_Session_001.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    /**
     * Add a new role
     */
    @PostMapping("/add")
    public ResponseEntity<Role> addRole(@RequestParam String roleName) {
        try {
            RoleEnum roleEnum = RoleEnum.valueOf(roleName.toUpperCase());
            Role savedRole = roleService.addRole(roleEnum);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
        } catch (IllegalArgumentException e) {
            log.error("Invalid role name: {}", roleName);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (Exception e) {
            log.error("Error saving role: {}", roleName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            return ResponseEntity.ok(roleService.getAllRoles());
        } catch (Exception e) {
            log.error("Error fetching roles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch roles");
        }
    }

}
