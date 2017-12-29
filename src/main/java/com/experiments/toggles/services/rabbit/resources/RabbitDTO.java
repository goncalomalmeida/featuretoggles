package com.experiments.toggles.services.rabbit.resources;

import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.persistence.entities.Toggle;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

public final class RabbitDTO {

    private RabbitDTO() {

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder(builderClassName = "Builder", toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SystemToggleDTO implements Serializable {

        private static final long serialVersionUID = 7807150122033510814L;

        private ToggleDTO toggle;

        private SystemDTO system;

        private boolean enabled;

        private boolean allowed;

        public static SystemToggleDTO from(SystemToggle systemToggle) {
            if (systemToggle == null) {
                return null;
            }

            return SystemToggleDTO.builder()
                    .enabled(systemToggle.isEnabled())
                    .allowed(systemToggle.isAllowed())
                    .system(SystemDTO.from(systemToggle.getSystem()))
                    .toggle(ToggleDTO.from(systemToggle.getToggle()))
                    .build();
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder(builderClassName = "Builder", toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ToggleDTO implements Serializable {

        private static final long serialVersionUID = 1787378066559850569L;

        private UUID id;

        private String name;

        private String description;

        static ToggleDTO from(Toggle toggle) {
            if (toggle == null) {
                return null;
            }

            return ToggleDTO.builder()
                    .description(toggle.getDescription())
                    .id(toggle.getId())
                    .name(toggle.getName())
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder(builderClassName = "Builder", toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class SystemDTO implements Serializable {

        private static final long serialVersionUID = -7083850718647058485L;

        private UUID id;

        private String name;

        private String description;

        private String systemVersion;

        static SystemDTO from(System system) {
            if (system == null) {
                return null;
            }

            return SystemDTO.builder()
                    .description(system.getDescription())
                    .name(system.getName())
                    .id(system.getId())
                    .systemVersion(system.getSystemVersion())
                    .build();
        }
    }
}