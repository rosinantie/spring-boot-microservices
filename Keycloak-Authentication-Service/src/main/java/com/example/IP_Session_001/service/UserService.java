package com.example.IP_Session_001.service;

import com.example.IP_Session_001.dto.request.UserLoginRequest;
import com.example.IP_Session_001.entity.Role;
import com.example.IP_Session_001.entity.UserLogin;
import com.example.IP_Session_001.Enum.RoleEnum;
import com.example.IP_Session_001.repository.RoleRepository;
import com.example.IP_Session_001.repository.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserLoginRepository userLoginRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public void saveUser(UserLoginRequest userRequest) {
        try {
            // Map RoleEnum from request to Role entities
            Set<Role> roles = userRequest.getRoleEnum().stream()
                    .map(roleEnum -> roleRepository.findByName(roleEnum)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleEnum)))
                    .collect(Collectors.toSet());

            // Build UserLogin entity
            UserLogin userLogin = UserLogin.builder()
                    .email(userRequest.getEmail())
                    .password(encoder.encode(userRequest.getPassword()))
                    .accountStatus("1") // active
                    .roles(roles)
                    .build();

            // Save to database
            userLoginRepository.save(userLogin);

            log.info("User saved successfully with email: {}", userRequest.getEmail());

        } catch (Exception e) {
            log.error("Error saving user login: {}", userRequest, e);
            throw new RuntimeException("Failed to save user"); // propagate exception
        }
    }


}
