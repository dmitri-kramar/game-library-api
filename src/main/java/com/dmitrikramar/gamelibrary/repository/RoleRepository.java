package com.dmitrikramar.gamelibrary.repository;

import com.dmitrikramar.gamelibrary.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for managing Role entities in the database. Provides CRUD operations.

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
