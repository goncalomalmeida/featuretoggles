package com.experiments.toggles.controllers.resources.transformers;

import com.experiments.toggles.controllers.resources.SystemResponse;
import com.experiments.toggles.persistence.entities.System;

/**
 * Transforms a {@link System} to a {@link SystemResponse}.
 * Usually it's not a good idea to expose JPA entities directly because:
 * <u>
 *     <li>they may contain fields that can't be exposed</li>
 *     <li>they are managed by a hibernate proxy so accessing certain fields can trigger undesired underlying
 *     database queries</li>
 * </u>
 */
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
