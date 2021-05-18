package com.real32.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.real32.models.Notes;

public interface NotesRepository extends JpaRepository<Notes, Long> {
	
	Notes findByTitle(final String title);
}
