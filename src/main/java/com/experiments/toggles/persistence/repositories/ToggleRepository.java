package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.Toggle;
import org.springframework.core.annotation.Order;

import java.util.UUID;

@Order(3)
public interface ToggleRepository extends GenericRepository<Toggle, UUID> {
}
