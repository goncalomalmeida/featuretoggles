package com.experiments.toggles.services;

import com.experiments.toggles.configuration.AmqpConfiguration;
import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.persistence.repositories.SystemRepository;
import com.experiments.toggles.persistence.repositories.SystemToggleRepository;
import com.experiments.toggles.persistence.repositories.ToggleRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

        SystemToggleDTO payload = SystemToggleDTO.from(systemToggle);
        rabbitService.send(AmqpConfiguration.TOGGLE_EXCHANGE, routingKey, payload, headers);

        return systemToggleRepository.save(systemToggle);
    }

    public List<SystemToggle> list(UUID systemUuid, String version) {

        final System system = systemRepository.findByIdAndSystemVersion(systemUuid, version);

        if (system == null) {
            throw new EntityNotFoundException("System " + systemUuid + " not found");
        }

        return systemToggleRepository.findMyToggles(system);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder(builderClassName = "Builder", toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class SystemToggleDTO implements Serializable {

        private static final long serialVersionUID = 7807150122033510814L;

        private ToggleDTO toggle;

        private SystemDTO system;

        private boolean enabled;

        private boolean allowed;

        static SystemToggleDTO from(SystemToggle systemToggle) {
            if (systemToggle == null) {
                return null;
            }

            return SystemToggleDTO.builder()
                    .enabled(systemToggle.isEnabled())
                    .allowed(systemToggle.isAllowed())
                    .system(SystemDTO.from(systemToggle.getSystem()))
                    .toggle(ToggleDTO.from(systemToggle.getToggle()))
                    .build();
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder(builderClassName = "Builder", toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ToggleDTO implements Serializable {

        private static final long serialVersionUID = 1787378066559850569L;

        private UUID id;

        private String name;

        private String description;

        static ToggleDTO from(Toggle toggle) {
            if (toggle == null) {
                return null;
            }

            return ToggleDTO.builder()
                    .description(toggle.getDescription())
                    .id(toggle.getId())
                    .name(toggle.getName())
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder(builderClassName = "Builder", toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class SystemDTO implements Serializable {

        private static final long serialVersionUID = -7083850718647058485L;

        private UUID id;

        private String name;

        private String description;

        private String systemVersion;

        static SystemDTO from(System system) {
            if (system == null) {
                return null;
            }

            return SystemDTO.builder()
                    .description(system.getDescription())
                    .name(system.getName())
                    .id(system.getId())
                    .systemVersion(system.getSystemVersion())
                    .build();
        }
    }
}
