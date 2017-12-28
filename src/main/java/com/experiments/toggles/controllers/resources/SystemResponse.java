package com.experiments.toggles.controllers.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemResponse implements Serializable {

    private static final long serialVersionUID = 4383668986899688452L;

    private UUID id;

    private String name;

    private String description;

}
