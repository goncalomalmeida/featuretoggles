package com.experiments.toggles.controllers.resources;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class SystemRequest implements Serializable {

    private static final long serialVersionUID = 6572252688105414945L;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String version;
}
