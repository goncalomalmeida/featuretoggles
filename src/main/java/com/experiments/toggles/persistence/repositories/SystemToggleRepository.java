package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.SystemToggle;
import org.springframework.data.repository.CrudRepository;

public interface SystemToggleRepository extends CrudRepository<SystemToggle, Long> {
}
