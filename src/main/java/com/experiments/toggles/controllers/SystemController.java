package com.experiments.toggles.controllers;

import com.experiments.toggles.controllers.resources.SystemRequest;
import com.experiments.toggles.controllers.resources.SystemResponse;
import com.experiments.toggles.controllers.resources.transformers.SystemTransformer;
import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * This controller exposes endpoints to manage {@link System}s
 */
@RestController
@RequestMapping("/api/systems")
public class SystemController {

    private final SystemService systemService;

    @Autowired
    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    /**
     * Creates a system using the body input and returns a HttpStatus.CREATED on success.
     *
     * @param request a DTO containing the necessary inputs for creating a {@link System}
     * @return a DTO representing the newly created system
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SystemResponse create(@Valid @RequestBody SystemRequest request) {

        final System system = systemService.create(request.getName(), request.getDescription(), request.getVersion());

        return SystemTransformer.transform(system);
    }
}
