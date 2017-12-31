package com.experiments.toggles.controllers.resources;

import com.experiments.toggles.persistence.entities.System;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * This DTO carries the necessary fields to create a new {@link System}.
 * Some basic annotation-based validations exist for a fail-fast approach.
 */
@Data
public class SystemRequest implements Serializable {

    private static final long serialVersionUID = 6572252688105414945L;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String version;
}
