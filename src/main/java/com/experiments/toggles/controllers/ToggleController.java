package com.experiments.toggles.controllers;

import com.experiments.toggles.controllers.resources.SystemToggleResponse;
import com.experiments.toggles.controllers.resources.ToggleRequest;
import com.experiments.toggles.controllers.resources.ToggleResponse;
import com.experiments.toggles.controllers.resources.transformers.SystemToggleTransformer;
import com.experiments.toggles.controllers.resources.transformers.ToggleTransformer;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.services.SystemToggleService;
import com.experiments.toggles.services.ToggleService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This controller exposes endpoints to manage {@link Toggle}s
 */
@RestController
@RequestMapping("/api/toggles")
public class ToggleController {

    static final String SYSTEM_ID_HEADER = "X-SystemId";

    static final String SYSTEM_VERSION_HEADER = "X-SystemVersion";

    private final ToggleService toggleService;

    private final SystemToggleService systemToggleService;

    @Autowired
    public ToggleController(ToggleService toggleService, SystemToggleService systemToggleService) {
        this.toggleService = toggleService;
        this.systemToggleService = systemToggleService;
    }

    /**
     * Creates a toggle using the body input and returns a HttpStatus.CREATED on success.
     *
     * @param request a DTO containing the necessary inputs for creating a {@link Toggle}
     * @return a DTO representing the newly created toggle
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ToggleResponse create(@Valid @RequestBody ToggleRequest request) {

        final Toggle toggle = toggleService.create(request.getName(), request.getDescription());

        return ToggleTransformer.transform(toggle);
    }

    /**
     * Returns a list of all the toggles that are available for the calling client.
     * The client is identified using the following headers:
     * <ul>
     * <li>X-SystemId</li>
     * <li>X-SystemVersion</li>
     * </ul>
     *
     * @param systemId identifies the system
     * @param version  identifies the system's version
     * @return a list of available toggles for the calling system
     */
    @GetMapping
    public List<SystemToggleResponse> list(@NotBlank @RequestHeader(value = SYSTEM_ID_HEADER) String systemId,
                                           @NotBlank @RequestHeader(value = SYSTEM_VERSION_HEADER) String version) {

        final UUID systemUuid = UUID.fromString(systemId);

        List<SystemToggle> systemToggles = systemToggleService.list(systemUuid, version);

        return systemToggles.stream().map(SystemToggleTransformer::transform).collect(Collectors.toList());
    }


}
