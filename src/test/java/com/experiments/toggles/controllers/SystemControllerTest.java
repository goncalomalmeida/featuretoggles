package com.experiments.toggles.controllers;

import com.experiments.toggles.TogglesApplicationTests;
import com.experiments.toggles.controllers.resources.SystemRequest;
import com.experiments.toggles.controllers.resources.SystemResponse;
import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.repositories.SystemRepository;
import com.experiments.toggles.utilities.EntityFaker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SystemControllerTest extends TogglesApplicationTests {

    @Autowired
    private SystemRepository systemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createSystemReturns201AndPersistsData() throws Exception {

        System systemToCreate = EntityFaker.system().build();
        SystemRequest systemRequest = new SystemRequest();
        systemRequest.setDescription(systemToCreate.getDescription());
        systemRequest.setName(systemToCreate.getName());
        systemRequest.setVersion(systemToCreate.getSystemVersion());

        String content = objectMapper.writeValueAsString(systemRequest);

        final MockHttpServletResponse response = mockMvc.perform(post("/api/systems")
                                                                         .with(httpBasic("admin", "admin"))
                                                                         .content(content)
                                                                         .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final SystemResponse systemResponse = objectMapper.readValue(response.getContentAsByteArray(),
                                                                     SystemResponse.class);

        final System existingSystem = systemRepository.findOne(systemResponse.getId());

        assertThat(systemResponse).isNotNull();
        assertThat(existingSystem).isNotNull();
        assertThat(systemResponse.getId()).isEqualTo(existingSystem.getId());
    }

    @Test
    public void createSystemWithEmptyNameReturnsBadRequest() throws Exception {

        System systemToCreate = EntityFaker.system().build();
        SystemRequest systemRequest = new SystemRequest();
        systemRequest.setVersion(systemToCreate.getSystemVersion());

        String content = objectMapper.writeValueAsString(systemRequest);

        mockMvc.perform(post("/api/systems")
                                .with(httpBasic("admin", "admin"))
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createWithoutAuthorizationTokenReturns401() throws Exception {
        mockMvc.perform(post("/api/systems"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createWithInvalidAuthorizationTokenReturns401() throws Exception {
        mockMvc.perform(post("/api/systems").with(httpBasic("dummy", "dummy")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createWithAuthorizationTokenButInsufficientPriviledgesReturns403() throws Exception {
        mockMvc.perform(post("/api/systems").with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }
}