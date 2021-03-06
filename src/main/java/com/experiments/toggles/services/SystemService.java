package com.experiments.toggles.services;

import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.repositories.SystemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class SystemService {

    private final SystemRepository systemRepository;

    @Autowired
    public SystemService(SystemRepository systemRepository) {
        this.systemRepository = systemRepository;
    }

    /**
     * Create a new {@link System} using the provided inputs.
     * Generates a {@link UUID} for it before persisting.
     *
     * @param name        the system name
     * @param description the system description
     * @param version     the system version
     * @return the newly created {@link System}
     */
    public System create(String name, String description, String version) {

        System system = new System();
        system.setId(UUID.randomUUID());
        system.setDescription(description);
        system.setName(name);
        system.setSystemVersion(version);

        log.info("Creating system with id '{}' name '{}' and description '{}'",
                 system.getId(),
                 system.getName(),
                 system.getDescription());

        return systemRepository.save(system);
    }
}
