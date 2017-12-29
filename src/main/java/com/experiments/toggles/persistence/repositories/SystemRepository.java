package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.System;

import java.util.UUID;

public interface SystemRepository extends GenericRepository<System, UUID> {

    System findByIdAndSystemVersion(UUID id, String version);
}
