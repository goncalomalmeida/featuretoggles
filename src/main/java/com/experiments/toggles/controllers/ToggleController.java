package com.experiments.toggles.controllers;

import com.experiments.toggles.controllers.resources.ToggleRequest;
import com.experiments.toggles.controllers.resources.ToggleResponse;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.services.ToggleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/toggles")
public class ToggleController {

    private final ToggleService toggleService;

    @Autowired
    public ToggleController(ToggleService toggleService) {
        this.toggleService = toggleService;
    }

    @PostMapping
    public ToggleResponse create(@Valid @RequestBody ToggleRequest request) {

        final Toggle toggle = toggleService.create(request.getName(), request.getDescription());

        return ToggleResponse
                .builder()
                .id(toggle.getId())
                .name(toggle.getName())
                .description(toggle.getDescription())
                .build();
    }
}
