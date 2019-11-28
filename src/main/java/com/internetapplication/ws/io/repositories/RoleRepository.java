package com.internetapplication.ws.io.repositories;

import com.internetapplication.ws.io.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String role_admin);
}