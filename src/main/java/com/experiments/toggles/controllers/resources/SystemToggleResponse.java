package com.experiments.toggles.controllers.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemToggleResponse implements Serializable {

    private static final long serialVersionUID = 4383668986899688452L;

    private ToggleResponse toggle;

    private SystemResponse system;

    private boolean enabled;

    private boolean allowed;
}
