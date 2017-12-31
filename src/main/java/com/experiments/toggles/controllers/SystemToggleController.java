package com.experiments.toggles.controllers;

import com.experiments.toggles.controllers.resources.SystemToggleRequest;
import com.experiments.toggles.controllers.resources.SystemToggleResponse;
import com.experiments.toggles.controllers.resources.transformers.SystemToggleTransformer;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.services.SystemToggleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

/**
 * This controller exposes endpoints to manage {@link SystemToggle}s
 */
@RestController
@RequestMapping("/api/systems/toggles")
public class SystemToggleController {

    private final SystemToggleService systemToggleService;

    @Autowired
    public SystemToggleController(SystemToggleService systemToggleService) {
        this.systemToggleService = systemToggleService;
    }

    /**
     * Creates a system toggle using the body input and returns a HttpStatus.OK on success.
     * <p>
     * PUT is an idempotent operation so calling it several times should always produce the same result.
     *
     * @param request a DTO containing the necessary inputs for creating a {@link SystemToggle}
     * @return a DTO representing the newly created system toggle
     */
    @PutMapping
    public SystemToggleResponse create(@Valid @RequestBody SystemToggleRequest request) {

        final UUID systemId = request.getSystemValue().map(UUID::fromString).orElse(null);
        final UUID toggleId = UUID.fromString(request.getToggle());

        final SystemToggle systemToggle = systemToggleService.create(systemId, toggleId, request.getEnabled(),
                                                                     request.getAllowed());

        return SystemToggleTransformer.transform(systemToggle);
    }

}
