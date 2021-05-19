package com.real32.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.real32.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(final String email);
	
	User findByFullname(final String fullname);

}
