package com.felipe.ecommerce_shipping_service.infrastructure.config;

import com.felipe.kafka.saga.replies.ShippingTransactionReply;
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
  public ProducerFactory<String, ShippingTransactionReply> producerFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    config.put(JsonSerializer.TYPE_MAPPINGS, "shippingTransactionReply:com.felipe.kafka.saga.replies.ShippingTransactionReply");
    return new DefaultKafkaProducerFactory<>(config);
  }

  @Bean
  public KafkaTemplate<String, ShippingTransactionReply> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
