package com.experiments.toggles.controllers.resources.transformers;

import com.experiments.toggles.controllers.resources.SystemResponse;
import com.experiments.toggles.persistence.entities.System;

public final class SystemTransformer {

    private SystemTransformer() {

    }

    public static SystemResponse transform(System system) {
        if (system == null) {
            return null;
        }

        return SystemResponse
                .builder()
                .id(system.getId())
                .name(system.getName())
                .description(system.getDescription())
                .version(system.getSystemVersion())
                .build();
    }
}
