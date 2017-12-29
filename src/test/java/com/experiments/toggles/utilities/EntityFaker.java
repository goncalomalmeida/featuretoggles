package com.experiments.toggles.utilities;

import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.persistence.entities.Toggle;
import com.github.javafaker.Faker;

import java.util.UUID;

public final class EntityFaker {

    private static Faker faker = new Faker();

    private EntityFaker() {

    }

    public static Toggle.Builder toggle() {
        return Toggle
                .builder()
                .description(faker.lorem().sentence())
                .name(faker.superhero().name())
                .id(UUID.randomUUID());
    }

    public static System.Builder system() {
        return System
                .builder()
                .id(UUID.randomUUID())
                .name(faker.app().name())
                .systemVersion(faker.app().version())
                .description(faker.lorem().sentence());
    }

    public static SystemToggle.Builder systemToggle(System system, Toggle toggle) {
        return SystemToggle
                .builder()
                .allowed(faker.bool().bool())
                .enabled(faker.bool().bool())
                .system(system)
                .toggle(toggle);
    }
}
