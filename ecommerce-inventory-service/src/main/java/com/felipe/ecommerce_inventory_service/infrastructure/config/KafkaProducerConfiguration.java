package com.felipe.ecommerce_inventory_service.infrastructure.config;

import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
  public ProducerFactory<String, InventoryTransactionReply> producerFactory() {
    final Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configs.put(JsonSerializer.TYPE_MAPPINGS, "inventoryTransactionReply:com.felipe.kafka.saga.replies.InventoryTransactionReply");
    return new DefaultKafkaProducerFactory<>(configs);
  }

  @Bean
  public KafkaTemplate<String, InventoryTransactionReply> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
