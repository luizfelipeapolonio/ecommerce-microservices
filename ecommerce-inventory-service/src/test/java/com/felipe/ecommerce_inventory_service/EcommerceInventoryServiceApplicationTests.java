package com.felipe.ecommerce_inventory_service;

import com.felipe.ecommerce_inventory_service.testutils.OAuth2TestMockConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "test")
@Import({OAuth2TestMockConfiguration.class})
class EcommerceInventoryServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
