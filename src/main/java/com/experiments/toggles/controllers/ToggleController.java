package com.experiments.toggles.controllers;

import com.experiments.toggles.controllers.resources.SystemToggleResponse;
import com.experiments.toggles.controllers.resources.transformers.SystemToggleTransformer;
import com.experiments.toggles.controllers.resources.ToggleRequest;
import com.experiments.toggles.controllers.resources.ToggleResponse;
import com.experiments.toggles.controllers.resources.transformers.ToggleTransformer;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.services.SystemToggleService;
import com.experiments.toggles.services.ToggleService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/toggles")
public class ToggleController {

    private final ToggleService toggleService;

    private final SystemToggleService systemToggleService;

    @Autowired
    public ToggleController(ToggleService toggleService, SystemToggleService systemToggleService) {
        this.toggleService = toggleService;
        this.systemToggleService = systemToggleService;
    }

    @PostMapping
    public ToggleResponse create(@Valid @RequestBody ToggleRequest request) {

        final Toggle toggle = toggleService.create(request.getName(), request.getDescription());

        return ToggleTransformer.transform(toggle);
    }

    @GetMapping
    public List<SystemToggleResponse> list(@NotBlank @RequestHeader(value = "X-SystemId") String systemId,
                                           @NotBlank @RequestHeader(value = "X-SystemVersion") String version) {

        final UUID systemUuid = UUID.fromString(systemId);

        List<SystemToggle> systemToggles = systemToggleService.list(systemUuid, version);

        return systemToggles.stream().map(SystemToggleTransformer::transform).collect(Collectors.toList());
    }


}
