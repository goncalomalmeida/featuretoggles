package com.experiments.toggles.controllers.resources;

import com.experiments.toggles.persistence.entities.Toggle;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * This DTO carries the necessary fields to create a new {@link Toggle}.
 * Some basic annotation-based validations exist for a fail-fast approach.
 */
@Data
public class ToggleRequest implements Serializable {

    private static final long serialVersionUID = 3790020477688031478L;

    @NotBlank
    private String name;

    private String description;
}
