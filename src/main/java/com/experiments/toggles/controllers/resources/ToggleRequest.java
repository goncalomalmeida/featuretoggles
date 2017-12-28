package com.experiments.toggles.controllers.resources;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class ToggleRequest implements Serializable {

    private static final long serialVersionUID = 3790020477688031478L;

    @NotBlank
    private String name;

    private String description;
}
