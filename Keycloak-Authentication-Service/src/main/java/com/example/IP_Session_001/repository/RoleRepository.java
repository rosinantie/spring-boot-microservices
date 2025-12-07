package com.example.IP_Session_001.repository;

import com.example.IP_Session_001.Enum.RoleEnum;
import com.example.IP_Session_001.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);

    Optional<Role> findByName(RoleEnum name);

}
