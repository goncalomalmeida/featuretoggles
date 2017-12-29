package com.experiments.toggles.controllers;

import com.experiments.toggles.TogglesApplicationTests;
import com.experiments.toggles.controllers.resources.SystemToggleResponse;
import com.experiments.toggles.controllers.resources.ToggleRequest;
import com.experiments.toggles.controllers.resources.ToggleResponse;
import com.experiments.toggles.persistence.entities.System;
import com.experiments.toggles.persistence.entities.Toggle;
import com.experiments.toggles.persistence.repositories.SystemRepository;
import com.experiments.toggles.persistence.repositories.ToggleRepository;
import com.experiments.toggles.utilities.EntityFaker;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ToggleRepository toggleRepository;

    @Autowired
    private SystemRepository systemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createToggleReturns201AndPersistsData() throws Exception {

        Toggle toggleToCreate = EntityFaker.toggle();
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

        Toggle toggleToCreate = EntityFaker.toggle();
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

//        final Toggle toggle = toggleRepository.save(EntityFaker.toggle());

        final System system = systemRepository.save(EntityFaker.system());

        final MockHttpServletResponse response = mockMvc.perform(get("/api/toggles")
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
        List<SystemToggleResponse> systemToggles = objectMapper.readValue(response.getContentAsByteArray(), type);

        assertThat(systemToggles).isNotNull();
    }

    @Test
    public void listTogglesAllowedForSpecificSystemReturnsThemPlusTheOnesAllowedForAllSystems() throws Exception {

    }

    @Test
    public void listTogglesNotAllowedForSpecificSystemAndAllowedForAllTheOtherOnesDoesntReturnAnything() throws
                                                                                                         Exception {

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
    public void listWithAuthorizationTokenButInsufficientPriviledgesReturns403() throws Exception {
        mockMvc.perform(get("/api/toggles").with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

}