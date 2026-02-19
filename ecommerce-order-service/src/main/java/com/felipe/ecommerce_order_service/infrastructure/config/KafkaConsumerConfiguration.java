package com.felipe.ecommerce_order_service.infrastructure.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServer;
  private static final String DESERIALIZER_TYPE_MAPPINGS = typeMappings();

  @Bean
  public ConsumerFactory<String, Object> consumerFactory() {
    final Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, "order-service");
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    configs.put(JsonDeserializer.TYPE_MAPPINGS, DESERIALIZER_TYPE_MAPPINGS);
    configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return new DefaultKafkaConsumerFactory<>(configs);
  }

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
    final ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  private static String typeMappings() {
    String[] types = {
      "inventoryTransactionReply:com.felipe.kafka.saga.replies.InventoryTransactionReply"
    };
    return types[0];
  }
}
