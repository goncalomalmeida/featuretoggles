package com.experiments.toggles.controllers;

import com.experiments.toggles.controllers.resources.ToggleResponse;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.services.ToggleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToggleController {

    @Autowired
    private ToggleService toggleService;

    @GetMapping("/api/toggles")
    public ToggleResponse addToggle() {

        final Toggle toggle = toggleService.addToggle();

        return ToggleResponse
                .builder()
                .id(toggle.getId())
                .name(toggle.getName())
                .description(toggle.getDescription())
                .build();
    }
}
