package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.System;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SystemRepository extends CrudRepository<System, UUID> {

    System findByIdAndSystemVersion(UUID id, String version);
}
