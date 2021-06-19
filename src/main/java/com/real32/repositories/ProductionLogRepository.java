package com.real32.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.real32.models.Mount;
import com.real32.models.ProductionLog;
import com.real32.models.Real32Unit;
import com.real32.models.User;

public interface ProductionLogRepository extends JpaRepository<ProductionLog, Long> {

	List<ProductionLog> findByUser(final User user);

	List<ProductionLog> findByReal32Unit(final Real32Unit real32Unit);

	List<ProductionLog> findByMount(final Mount mount);

}
