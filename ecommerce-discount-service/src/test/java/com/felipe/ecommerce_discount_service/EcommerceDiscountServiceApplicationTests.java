package com.felipe.ecommerce_discount_service;

import com.felipe.ecommerce_discount_service.testutils.OAuth2TestMockConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "test")
@Import({OAuth2TestMockConfiguration.class})
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
class EcommerceDiscountServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
