package com.experiments.toggles.controllers.resources.transformers;

import com.experiments.toggles.controllers.resources.ToggleResponse;
import com.experiments.toggles.persistence.entities.Toggle;

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
