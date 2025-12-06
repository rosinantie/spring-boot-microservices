package com.example.IP_Session_001.repository;

import com.example.IP_Session_001.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, UUID> {
    Optional<UserLogin> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
