package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.SystemToggle;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.UUID;

@Order(1)
public interface SystemToggleRepository extends GenericRepository<SystemToggle, Long> {

    List<SystemToggle> findAllByAllowedTrueAndSystemIsNull();

    SystemToggle findBySystemIdAndToggleId(UUID systemId, UUID toggleId);
}
