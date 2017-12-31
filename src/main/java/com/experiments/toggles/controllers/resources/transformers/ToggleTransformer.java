package com.experiments.toggles.controllers.resources.transformers;

import com.experiments.toggles.controllers.resources.ToggleResponse;
import com.experiments.toggles.persistence.entities.Toggle;

/**
 * Transforms a {@link Toggle} to a {@link ToggleResponse}.
 * Usually it's not a good idea to expose JPA entities directly because:
 * <u>
 *     <li>they may contain fields that can't be exposed</li>
 *     <li>they are managed by a hibernate proxy so accessing certain fields can trigger undesired underlying
 *     database queries</li>
 * </u>
 */
public final class ToggleTransformer {

    private ToggleTransformer() {
        
    }

    public static ToggleResponse transform(Toggle toggle) {
        if (toggle == null) {
            return null;
        }

        return ToggleResponse
                .builder()
                .id(toggle.getId())
                .name(toggle.getName())
                .description(toggle.getDescription())
                .build();
    }
}
