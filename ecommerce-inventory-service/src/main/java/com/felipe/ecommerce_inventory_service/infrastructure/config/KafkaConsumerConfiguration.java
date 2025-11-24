package com.felipe.ecommerce_inventory_service.infrastructure.config;

import com.felipe.kafka.ExpiredPromotionKafkaDTO;
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

  @Bean
  public ConsumerFactory<String, ExpiredPromotionKafkaDTO> consumerFactory() {
    final Map<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, "promotion-group");
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    configs.put(JsonDeserializer.TYPE_MAPPINGS, "expiredPromotionKafkaDTO:com.felipe.kafka.ExpiredPromotionKafkaDTO");
    configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return new DefaultKafkaConsumerFactory<>(configs);
  }

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, ExpiredPromotionKafkaDTO>> kafkaListenerContainerFactory() {
    final ConcurrentKafkaListenerContainerFactory<String, ExpiredPromotionKafkaDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
