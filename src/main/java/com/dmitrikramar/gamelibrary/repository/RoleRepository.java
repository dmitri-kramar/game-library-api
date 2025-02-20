package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Role;
import com.dmitrikramar.gamelibrary.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repository interface for performing CRUD operations on Role entities.

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Finds a Role by its name.
    // Returns an Optional<Role> to handle the case where the role may not exist.
    Optional<Role> findByName(RoleName name);
}
