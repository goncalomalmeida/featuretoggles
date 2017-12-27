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

    @Autowired
    private ToggleRepository toggleRepository;

    public Toggle addToggle() {

        Toggle toggle = new Toggle();
        toggle.setId(UUID.randomUUID());
        toggle.setName("randomName");
        toggle.setDescription("randomDescription");

        log.info("Creating toggle with id {}", toggle.getId());

        return toggleRepository.save(toggle);
    }
}
