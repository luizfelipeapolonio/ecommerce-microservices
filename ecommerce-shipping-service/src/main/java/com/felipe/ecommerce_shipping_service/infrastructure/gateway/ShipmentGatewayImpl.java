package com.felipe.ecommerce_shipping_service.infrastructure.gateway;

import com.felipe.ecommerce_shipping_service.core.application.gateway.ShipmentGateway;
import com.felipe.ecommerce_shipping_service.core.domain.Shipment;
import com.felipe.ecommerce_shipping_service.infrastructure.mappers.ShipmentEntityMapper;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipment.ShipmentEntity;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.repositories.ShipmentRepository;
import com.felipe.ecommerce_shipping_service.infrastructure.services.ShipmentSchedulerService;
import com.felipe.kafka.EmailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShipmentGatewayImpl implements ShipmentGateway {
  private final ShipmentRepository shipmentRepository;
  private final ShipmentEntityMapper shipmentEntityMapper;
  private final ShipmentSchedulerService shipmentSchedulerService;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(ShipmentGatewayImpl.class);

  public ShipmentGatewayImpl(ShipmentRepository shipmentRepository,
                             ShipmentEntityMapper shipmentEntityMapper,
                             ShipmentSchedulerService shipmentSchedulerService,
                             KafkaTemplate<String, Object> kafkaTemplate) {
    this.shipmentRepository = shipmentRepository;
    this.shipmentEntityMapper = shipmentEntityMapper;
    this.shipmentSchedulerService = shipmentSchedulerService;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public Shipment createShipment(Shipment shipment) {
    ShipmentEntity savedEntity = this.shipmentRepository.save(this.shipmentEntityMapper.toEntity(shipment));
    this.shipmentSchedulerService.scheduleShipmentForStatusMutation(savedEntity);

    EmailDTO emailDTO = new EmailDTO()
      .setOrderId(savedEntity.getOrderId())
      .setSubject(EmailDTO.Subject.PREPARING_SHIPMENT)
      .setTrackingCode(savedEntity.getTrackingCode());

    this.kafkaTemplate.send("order.emails", emailDTO)
      .whenComplete((result, exception) -> {
        if (exception == null) {
          logger.info("Preparing shipment email posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
        }
      });
    return this.shipmentEntityMapper.toDomain(savedEntity);
  }
}
