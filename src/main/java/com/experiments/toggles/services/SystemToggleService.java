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

    /**
     * Associates a toggle to a system.
     *
     * @param systemId the system id that is being associated. If null it means that this toggle is universally
     *                 available to all systems.
     * @param toggleId the toggle id that is being associated
     * @param enabled  indicates if the toggle is enabled (true) or not (false)
     * @param allowed  if true the association will be white-listed and returned when asked, if false the association
     *                 will be black-listed and excluded when asked
     * @return the newly created association between a system and a toggle
     */
    public SystemToggle create(UUID systemId, UUID toggleId, boolean enabled, boolean allowed) {

        final System system = systemRepository.findOne(systemId);
        final Toggle toggle = toggleRepository.findOne(toggleId);

        SystemToggle systemToggle = new SystemToggle();
        systemToggle.setSystem(system);
        systemToggle.setToggle(toggle);
        systemToggle.setEnabled(enabled);
        systemToggle.setAllowed(allowed);

        log.info("Associating toggle with id {} to system id {}", toggleId, systemId);

        final SystemToggle persistedSystemToggle = systemToggleRepository.save(systemToggle);

        log.info("Building a message to publish to RabbitMQ");

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

        // delegate the publishing to RabbitService.send
        rabbitService.send(AmqpConfiguration.TOGGLE_EXCHANGE, routingKey, payload, headers);

        return persistedSystemToggle;
    }

    /**
     * Fetches all toggle for a given system/version.
     * It merges the toggles directly associated to this system and the universally available toggles. The former is
     * always more important than the latter
     *
     * @param systemId system identifier
     * @param version  system version
     * @return a list of {@link SystemToggle} that are available for the calling system
     */
    public List<SystemToggle> list(UUID systemId, String version) {

        final System system = systemRepository.findByIdAndSystemVersion(systemId, version);

        if (system == null) {
            throw new EntityNotFoundException("System " + systemId + " not found");
        }

        // fetch the toggles directly associated to this system
        final List<SystemToggle> systemToggles = new ArrayList<>(system.getSystemToggles());

        final Set<Toggle> toggles = system
                .getSystemToggles()
                .stream()
                .map(SystemToggle::getToggle)
                .collect(Collectors.toSet());

        // fetch all the universally white-listed toggles
        final List<SystemToggle> universalSystemToggles = systemToggleRepository.findAllByAllowedTrueAndSystemIsNull();

        // merge the two lists, prioritizing the toggles that are directly associated
        universalSystemToggles.forEach(st -> {
            if (!toggles.contains(st.getToggle())) {
                systemToggles.add(st);
            }
        });

        // at the end filter out all the 'not allowed'
        return systemToggles.stream().filter(SystemToggle::isAllowed).collect(Collectors.toList());
    }


}
