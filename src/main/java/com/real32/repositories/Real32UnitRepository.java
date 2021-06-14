package com.real32.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.real32.models.Real32Unit;

public interface Real32UnitRepository extends JpaRepository<Real32Unit,Long> {

	Real32Unit findBySerial(final String serial);
	
	List<Real32Unit> findByAssembledOn(final String assembledOn);
	
	List<Real32Unit> findByAssembledBy(final String assembledBy);
}
