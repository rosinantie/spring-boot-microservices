package com.example.IP_Session_001.service;

import com.example.IP_Session_001.Enum.RoleEnum;
import com.example.IP_Session_001.entity.Role;
import com.example.IP_Session_001.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public Role addRole(RoleEnum roleEnum) {
        // Check if role already exists
        return roleRepository.findByName(roleEnum)
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name(roleEnum)
                            .build();
                    Role saved = roleRepository.save(role);
                    log.info("Role saved: {}", saved.getName());
                    return saved;
                });
    }
}
