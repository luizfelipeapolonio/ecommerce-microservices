package com.felipe.ecommerce_discount_service.infrastructure.config;

import com.felipe.kafka.ExpiredPromotionKafkaDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServer;

  @Bean
  public ProducerFactory<String, ExpiredPromotionKafkaDTO> producerFactory() {
    final Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configs.put(JsonSerializer.TYPE_MAPPINGS, "expiredPromotionKafkaDTO:com.felipe.kafka.ExpiredPromotionKafkaDTO");
    return new DefaultKafkaProducerFactory<>(configs);
  }

  @Bean
  public KafkaTemplate<String, ExpiredPromotionKafkaDTO> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public NewTopic promotionTopic() {
    return TopicBuilder.name("expired-promotion").build();
  }
}
