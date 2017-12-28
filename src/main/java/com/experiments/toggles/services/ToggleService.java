package com.experiments.toggles.services;

import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.persistence.repositories.ToggleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ToggleService {

    private final ToggleRepository toggleRepository;

    @Autowired
    public ToggleService(ToggleRepository toggleRepository) {
        this.toggleRepository = toggleRepository;
    }

    public Toggle create(String name, String description) {
        Toggle toggle = new Toggle();
        toggle.setId(UUID.randomUUID());
        toggle.setName(name);
        toggle.setDescription(description);

        log.info("Creating toggle with id '{}' name '{}' and description '{}'",
                 toggle.getId(),
                 toggle.getName(),
                 toggle.getDescription());

        return toggleRepository.save(toggle);
    }
}
