package com.experiments.toggles.controllers.resources.transformers;

import com.experiments.toggles.controllers.resources.SystemToggleResponse;
import com.experiments.toggles.persistence.entities.SystemToggle;

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
