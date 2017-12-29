package com.experiments.toggles.persistence.repositories;

import com.experiments.toggles.persistence.entities.System;
import org.springframework.core.annotation.Order;

import java.util.UUID;

@Order(2)
public interface SystemRepository extends GenericRepository<System, UUID> {

    System findByIdAndSystemVersion(UUID id, String version);
}
