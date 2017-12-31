package com.experiments.toggles.controllers.resources.transformers;

import com.experiments.toggles.controllers.resources.SystemToggleResponse;
import com.experiments.toggles.persistence.entities.SystemToggle;

/**
 * Transforms a {@link SystemToggle} to a {@link SystemToggleResponse}.
 * Usually it's not a good idea to expose JPA entities directly because:
 * <u>
 *     <li>they may contain fields that can't be exposed</li>
 *     <li>they are managed by a hibernate proxy so accessing certain fields can trigger undesired underlying
 *     database queries</li>
 * </u>
 */
public final class SystemToggleTransformer {

    private SystemToggleTransformer() {

    }

    public static SystemToggleResponse transform(SystemToggle systemToggle) {
        return SystemToggleResponse
                .builder()
                .allowed(systemToggle.isAllowed())
                .enabled(systemToggle.isEnabled())
                .toggle(ToggleTransformer.transform(systemToggle.getToggle()))
                .system(SystemTransformer.transform(systemToggle.getSystem()))
                .build();
    }
}
