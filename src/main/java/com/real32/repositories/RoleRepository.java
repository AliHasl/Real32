package com.real32.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.real32.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRole(final String role);

}
