package com.experiments.toggles.services;

import com.experiments.toggles.configuration.AmqpConfiguration;
import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.persistence.repositories.SystemRepository;
import com.experiments.toggles.persistence.repositories.SystemToggleRepository;
import com.experiments.toggles.persistence.repositories.ToggleRepository;
import com.experiments.toggles.services.rabbit.RabbitService;
import com.experiments.toggles.services.rabbit.resources.RabbitDTO;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemToggleService {

    private final SystemToggleRepository systemToggleRepository;

    private final ToggleRepository toggleRepository;

    private final SystemRepository systemRepository;

    private final RabbitService rabbitService;

    @Autowired
    public SystemToggleService(SystemToggleRepository systemToggleRepository,
                               ToggleRepository toggleRepository,
                               SystemRepository systemRepository,
                               RabbitService rabbitService) {
        this.systemToggleRepository = systemToggleRepository;
        this.toggleRepository = toggleRepository;
        this.systemRepository = systemRepository;
        this.rabbitService = rabbitService;
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

        // the system id is part of the routing key
        String routingKey = "toggles.service" + Optional
                .ofNullable(system)
                .map(System::getId)
                .map(UUID::toString)
                .map(s -> "" + "." + s)
                .orElse("");

        Map<String, String> headers = ImmutableMap.of("event", "SYSTEM_TOGGLE_CHANGED",
                                                      "version", "V1",
                                                      "systemId", system.getId().toString());

        RabbitDTO.SystemToggleDTO payload = RabbitDTO.SystemToggleDTO.from(systemToggle);
        rabbitService.send(AmqpConfiguration.TOGGLE_EXCHANGE, routingKey, payload, headers);

        return systemToggleRepository.save(systemToggle);
    }

    public List<SystemToggle> list(UUID systemUuid, String version) {

        final System system = systemRepository.findByIdAndSystemVersion(systemUuid, version);

        if (system == null) {
            throw new EntityNotFoundException("System " + systemUuid + " not found");
        }

        final List<SystemToggle> systemToggles = new ArrayList<>(system.getSystemToggles());

        final Set<Toggle> toggles = system
                .getSystemToggles()
                .stream()
                .map(SystemToggle::getToggle)
                .collect(Collectors.toSet());

        final List<SystemToggle> universalSystemToggles = systemToggleRepository.findAllByAllowedTrueAndSystemIsNull();

        universalSystemToggles.forEach(st -> {
            if (!toggles.contains(st.getToggle())) {
                systemToggles.add(st);
            }
        });

        // at the end filter out all the 'not allowed'
        return systemToggles.stream().filter(SystemToggle::isAllowed).collect(Collectors.toList());
    }


}
