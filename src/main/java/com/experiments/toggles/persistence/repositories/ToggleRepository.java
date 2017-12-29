package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.Toggle;

import java.util.UUID;

public interface ToggleRepository extends GenericRepository<Toggle, UUID> {
}
