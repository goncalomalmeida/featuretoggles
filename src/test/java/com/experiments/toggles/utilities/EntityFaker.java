package com.experiments.toggles.utilities;

import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.persistence.entities.System;
import com.github.javafaker.Faker;

import java.util.UUID;

public final class EntityFaker {

    private static Faker faker = new Faker();

    private EntityFaker() {

    }

    public static Toggle toggle() {
        Toggle toggle = new Toggle();
        toggle.setDescription(faker.lorem().sentence());
        toggle.setName(faker.superhero().name());
        toggle.setId(UUID.randomUUID());

        return toggle;
    }

    public static System system() {
        System system = new System();
        system.setId(UUID.randomUUID());
        system.setName(faker.app().name());
        system.setSystemVersion(faker.app().version());
        system.setDescription(faker.lorem().sentence());

        return system;
    }
}
