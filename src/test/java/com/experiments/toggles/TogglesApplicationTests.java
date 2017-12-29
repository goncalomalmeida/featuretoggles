package com.experiments.toggles;

import com.experiments.toggles.controllers.SystemController;
import com.experiments.toggles.controllers.SystemToggleController;
import com.experiments.toggles.controllers.ToggleController;
import com.experiments.toggles.persistence.repositories.GenericRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TogglesApplicationTests {

	@Autowired
	private ToggleController toggleController;

	@Autowired
	private SystemController systemController;

	@Autowired
	private SystemToggleController systemToggleController;

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	private List<GenericRepository> repositories;

	@Before
	public void setUp() {
		repositories.forEach(r -> r.delete(r.findAll()));
	}

	@Test
	public void contextLoads() {

		assertThat(toggleController).isNotNull();
		assertThat(systemController).isNotNull();
		assertThat(systemToggleController).isNotNull();
	}

}
