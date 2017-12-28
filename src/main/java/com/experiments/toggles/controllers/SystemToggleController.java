package com.experiments.toggles.controllers;

import com.experiments.toggles.controllers.resources.SystemResponse;
import com.experiments.toggles.controllers.resources.SystemToggleRequest;
import com.experiments.toggles.controllers.resources.SystemToggleResponse;
import com.experiments.toggles.controllers.resources.ToggleResponse;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.services.SystemToggleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/systems/toggles")
public class SystemToggleController {

    private final SystemToggleService systemToggleService;

    @Autowired
    public SystemToggleController(SystemToggleService systemToggleService) {
        this.systemToggleService = systemToggleService;
    }

    @PutMapping
    public SystemToggleResponse create(@Valid @RequestBody SystemToggleRequest request) {

        final UUID systemId = request.getSystem().map(UUID::fromString).orElse(null);
        final UUID toggleId = UUID.fromString(request.getToggle());

        final SystemToggle systemToggle = systemToggleService.create(systemId, toggleId, request.getEnabled(),
                                                                     request.getAllowed());

        return buildResponse(systemToggle);
    }

    private SystemToggleResponse buildResponse(SystemToggle systemToggle) {
        final SystemToggleResponse.Builder builder = SystemToggleResponse
                .builder()
                .allowed(systemToggle.isAllowed())
                .toggle(ToggleResponse
                                .builder()
                                .id(systemToggle.getToggle().getId())
                                .name(systemToggle.getToggle().getName())
                                .description(systemToggle.getToggle().getDescription())
                                .build())
                .enabled(systemToggle.isEnabled());

        if (systemToggle.getSystem() != null) {
            builder.system(SystemResponse
                                   .builder()
                                   .id(systemToggle.getSystem().getId())
                                   .name(systemToggle.getSystem().getName())
                                   .description(systemToggle.getSystem().getDescription())
                                   .build());
        }

        return builder.build();
    }
}
