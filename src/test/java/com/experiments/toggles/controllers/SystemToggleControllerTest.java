package com.experiments.toggles.controllers;

import com.experiments.toggles.TogglesApplicationTests;
import com.experiments.toggles.controllers.resources.SystemToggleRequest;
import com.experiments.toggles.controllers.resources.SystemToggleResponse;
import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.SystemToggle;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.utilities.EntityFaker;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SystemToggleControllerTest extends TogglesApplicationTests {

    @Test
    public void createSystemToggleReturns200AndPersistsData() throws Exception {

        Toggle toggleToCreate = toggleRepository.save(EntityFaker.toggle().build());
        System system = systemRepository.save(EntityFaker.system().build());

        SystemToggleRequest systemToggleRequest = new SystemToggleRequest();
        systemToggleRequest.setAllowed(true);
        systemToggleRequest.setEnabled(true);
        systemToggleRequest.setSystem(system.getId().toString());
        systemToggleRequest.setToggle(toggleToCreate.getId().toString());

        String content = objectMapper.writeValueAsString(systemToggleRequest);

        final MockHttpServletResponse response = mockMvc.perform(put("/api/systems/toggles")
                                                                         .with(httpBasic("admin", "admin"))
                                                                         .content(content)
                                                                         .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final SystemToggleResponse systemToggleResponse = objectMapper.readValue(response.getContentAsByteArray(),
                                                                                 SystemToggleResponse.class);

        final SystemToggle existingSystemToggle = systemToggleRepository.findBySystemIdAndToggleId(
                systemToggleResponse.getSystem().getId(), systemToggleResponse.getToggle().getId());

        assertThat(systemToggleResponse).isNotNull();
        assertThat(existingSystemToggle).isNotNull();
        assertThat(systemToggleResponse.getToggle().getId()).isEqualTo(existingSystemToggle.getToggle().getId());
        assertThat(systemToggleResponse.getSystem().getId()).isEqualTo(existingSystemToggle.getSystem().getId());


    }

    @Test
    public void createSystemToggleWithEmptyToggleReturnsBadRequest() throws Exception {

        SystemToggleRequest systemToggleRequest = new SystemToggleRequest();
        systemToggleRequest.setAllowed(true);
        systemToggleRequest.setEnabled(true);

        String content = objectMapper.writeValueAsString(systemToggleRequest);

        mockMvc.perform(put("/api/systems/toggles")
                                .with(httpBasic("admin", "admin"))
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void listWithoutAuthorizationTokenReturns401() throws Exception {
        mockMvc.perform(put("/api/systems/toggles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void listWithInvalidAuthorizationTokenReturns401() throws Exception {
        mockMvc.perform(put("/api/systems/toggles").with(httpBasic("dummy", "dummy")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void listWithAuthorizationTokenButInsufficientPrivilegesReturns403() throws Exception {
        mockMvc.perform(put("/api/systems/toggles").with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }
}