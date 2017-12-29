package com.experiments.toggles.controllers;

import com.experiments.toggles.TogglesApplicationTests;
import com.experiments.toggles.controllers.resources.SystemToggleResponse;
import com.experiments.toggles.controllers.resources.ToggleRequest;
import com.experiments.toggles.controllers.resources.ToggleResponse;
import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.utilities.EntityFaker;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ToggleControllerTest extends TogglesApplicationTests {

    @Test
    public void createToggleReturns201AndPersistsData() throws Exception {

        Toggle toggleToCreate = EntityFaker.toggle().build();
        ToggleRequest toggleRequest = new ToggleRequest();
        toggleRequest.setDescription(toggleToCreate.getDescription());
        toggleRequest.setName(toggleToCreate.getName());

        String content = objectMapper.writeValueAsString(toggleRequest);

        final MockHttpServletResponse response = mockMvc.perform(post("/api/toggles")
                                                                         .with(httpBasic("admin", "admin"))
                                                                         .content(content)
                                                                         .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final ToggleResponse toggleResponse = objectMapper.readValue(response.getContentAsByteArray(),
                                                                     ToggleResponse.class);

        final Toggle existingToggle = toggleRepository.findOne(toggleResponse.getId());

        assertThat(toggleResponse).isNotNull();
        assertThat(existingToggle).isNotNull();
        assertThat(toggleResponse.getId()).isEqualTo(existingToggle.getId());

    }

    @Test
    public void createToggleWithEmptyNameReturnsBadRequest() throws Exception {

        Toggle toggleToCreate = EntityFaker.toggle().build();
        ToggleRequest toggleRequest = new ToggleRequest();
        toggleRequest.setDescription(toggleToCreate.getDescription());

        String content = objectMapper.writeValueAsString(toggleRequest);

        mockMvc.perform(post("/api/toggles")
                                .with(httpBasic("admin", "admin"))
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void listTogglesAllowedForAllSystemsReturnsEverything() throws Exception {

        // prepare data
        final Toggle toggleForAllSystems = toggleRepository.save(EntityFaker.toggle().build());
        final Toggle toggleForSystem = toggleRepository.save(EntityFaker.toggle().build());
        final Toggle toggleForAnotherSystem = toggleRepository.save(EntityFaker.toggle().build());

        final System anotherSystem = systemRepository.save(EntityFaker.system().build());

        final System system = systemRepository.save(EntityFaker.system().build());

        // associate the first toggle to 'all systems'
        final SystemToggle systemToggleAllSystems = systemToggleRepository.save(
                EntityFaker.systemToggle(null, toggleForAllSystems).allowed(true).build());

        // associate the second toggle to 'system'
        final SystemToggle systemToggleSpecificForSystem = systemToggleRepository.save(
                EntityFaker.systemToggle(system, toggleForSystem).allowed(true).build());

        // associate the third toggle to 'another system'
        systemToggleRepository.save(
                EntityFaker.systemToggle(anotherSystem, toggleForAnotherSystem).allowed(true).build());

        // ask for available toggles for 'system'
        List<SystemToggleResponse> systemToggles = getMyToggles(system);

        assertThat(systemToggles)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .extracting(SystemToggleResponse::getToggle)
                .extracting(ToggleResponse::getId)
                .containsExactlyInAnyOrder(toggleForAllSystems.getId(), toggleForSystem.getId())
                .doesNotContain(toggleForAnotherSystem.getId());

        assertThat(systemToggles)
                .extracting(SystemToggleResponse::isEnabled)
                .containsExactlyInAnyOrder(systemToggleAllSystems.isEnabled(),
                                           systemToggleSpecificForSystem.isEnabled());
    }

    @Test
    public void listTogglesAllowedForSpecificSystemAndAllSystemsReturnsReturnsTheSpecificOneOnly() throws
                                                                                                   Exception {
        // prepare data
        final Toggle toggle1 = toggleRepository.save(EntityFaker.toggle().build());
        final Toggle toggle2 = toggleRepository.save(EntityFaker.toggle().build());

        final System system = systemRepository.save(EntityFaker.system().build());

        // associate the first toggle to 'all systems'
        systemToggleRepository.save(EntityFaker.systemToggle(null, toggle1).allowed(true).enabled(true).build());
        systemToggleRepository.save(EntityFaker.systemToggle(null, toggle2).allowed(true).enabled(false).build());

        // associate the second toggle to 'system'
        systemToggleRepository.save(EntityFaker.systemToggle(system, toggle1).allowed(true).enabled(false).build());
        systemToggleRepository.save(EntityFaker.systemToggle(system, toggle2).allowed(true).enabled(true).build());

        // ask for available toggles for 'system'
        List<SystemToggleResponse> systemToggles = getMyToggles(system);

        // the list should only have two elements (the specific toggles instead of the generic one)
        assertThat(systemToggles)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .extracting(SystemToggleResponse::isEnabled)
                .containsExactly(false, true);
    }

    @Test
    public void listTogglesNotAllowedForSpecificSystemAndAllowedForAllTheOtherOnesDoesntReturnAnything() throws
                                                                                                         Exception {
        final Toggle toggle = toggleRepository.save(EntityFaker.toggle().build());

        final System system = systemRepository.save(EntityFaker.system().build());

        // associate the first toggle to 'all systems'
        systemToggleRepository.save(EntityFaker.systemToggle(null, toggle).allowed(true).enabled(true).build());

        // associate the second toggle to 'system'
        systemToggleRepository.save(EntityFaker.systemToggle(system, toggle).allowed(false).enabled(false).build());

        // ask for available toggles for 'system'
        List<SystemToggleResponse> systemToggles = getMyToggles(system);

        assertThat(systemToggles)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void listWithoutAuthorizationTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/toggles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void listWithInvalidAuthorizationTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/toggles").with(httpBasic("dummy", "dummy")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void listWithAuthorizationTokenButInsufficientPrivilegesReturns403() throws Exception {
        mockMvc.perform(get("/api/toggles").with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }


    private List<SystemToggleResponse> getMyToggles(System system) throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(
                get("/api/toggles")
                        .with(httpBasic("admin", "admin"))
                        .header(ToggleController.SYSTEM_ID_HEADER,
                                system.getId().toString())
                        .header(ToggleController.SYSTEM_VERSION_HEADER,
                                system.getSystemVersion()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        JavaType type = objectMapper
                .getTypeFactory()
                .constructParametrizedType(List.class, List.class, SystemToggleResponse.class);
        return objectMapper.readValue(response.getContentAsByteArray(), type);
    }
}