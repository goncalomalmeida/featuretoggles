package com.experiments.toggles.controllers.resources;

import com.experiments.toggles.persistence.entities.SystemToggle;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;

/**
 * This DTO carries the necessary fields to create a new {@link SystemToggle}.
 * Some basic annotation-based validations exist for a fail-fast approach.
 */
@Data
public class SystemToggleRequest implements Serializable {

    private static final long serialVersionUID = 4268743036584103142L;
    /**
     * Empty service means that this toggle should be applied to all services
     */
    private String system;

    @NotNull
    private String toggle;

    @NotNull
    private Boolean enabled;

    @NotNull
    private Boolean allowed;

    public Optional<String> getSystemValue() {
        return Optional.ofNullable(system);
    }
}
