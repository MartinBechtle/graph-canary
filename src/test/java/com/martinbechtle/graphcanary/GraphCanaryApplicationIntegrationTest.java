package com.martinbechtle.graphcanary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Generic test that runs against the actual Spring context just to verify if it bootstraps correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphCanaryApplicationIntegrationTest {

	@Test
	public void contextLoads() {
	}

}
