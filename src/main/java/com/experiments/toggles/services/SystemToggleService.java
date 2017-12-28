package com.experiments.toggles.services;

import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.persistence.repositories.SystemRepository;
import com.experiments.toggles.persistence.repositories.SystemToggleRepository;
import com.experiments.toggles.persistence.repositories.ToggleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class SystemToggleService {

    private final SystemToggleRepository systemToggleRepository;

    private final ToggleRepository toggleRepository;

    private final SystemRepository systemRepository;

    @Autowired
    public SystemToggleService(SystemToggleRepository systemToggleRepository, ToggleRepository toggleRepository, SystemRepository systemRepository) {
        this.systemToggleRepository = systemToggleRepository;
        this.toggleRepository = toggleRepository;
        this.systemRepository = systemRepository;
    }

    public SystemToggle create(UUID systemId, UUID toggleId, boolean enabled, boolean allowed) {

        final System system = systemRepository.findOne(systemId);
        final Toggle toggle = toggleRepository.findOne(toggleId);

        SystemToggle systemToggle = new SystemToggle();
        systemToggle.setSystem(system);
        systemToggle.setToggle(toggle);
        systemToggle.setEnabled(enabled);
        systemToggle.setAllowed(allowed);

        log.info("Associating toggle with id {} to system id {}", toggleId, systemId);

        return systemToggleRepository.save(systemToggle);
    }
}
