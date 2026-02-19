package com.felipe.ecommerce_order_service.infrastructure.config;

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
  private static final String SERIALIZER_TYPE_MAPPINGS = typeMappings();

  @Bean
  public ProducerFactory<String, Object> producerFactory() {
    final Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configs.put(JsonSerializer.TYPE_MAPPINGS, SERIALIZER_TYPE_MAPPINGS);
    return new DefaultKafkaProducerFactory<>(configs);
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public NewTopic orderTransactionInventoryCommands() {
    return TopicBuilder.name("order.order_transaction.inventory.commands")
      .partitions(2)
      .build();
  }

  @Bean
  public NewTopic orderTransactionReplies() {
    return TopicBuilder.name("order.order_transaction.replies")
      .partitions(2)
      .build();
  }

  private static String typeMappings() {
    String[] types = {
      "inventoryTransactionCreateCommand:com.felipe.kafka.saga.commands.InventoryTransactionCreateCommand",
      "inventoryTransactionCancelCommand:com.felipe.kafka.saga.commands.InventoryTransactionCancelCommand",
    };
    return String.join(", ", types);
  }
}
