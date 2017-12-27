package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.Toggle;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ToggleRepository extends CrudRepository<Toggle, UUID> {
}
