package com.real32.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.real32.models.Mount;

public interface MountRepository extends JpaRepository<Mount, Long> {

	Mount findBySerial(final String serial);

	List<Mount> findByManufacturedOn(final String manufacturedOn);

	List<Mount> findByManufacturedBy(final String manufacturedBy);
}
