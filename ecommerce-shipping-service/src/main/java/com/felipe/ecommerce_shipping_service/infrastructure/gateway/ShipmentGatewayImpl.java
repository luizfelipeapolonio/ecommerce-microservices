package com.felipe.ecommerce_shipping_service.infrastructure.gateway;

import com.felipe.ecommerce_shipping_service.core.application.gateway.ShipmentGateway;
import com.felipe.ecommerce_shipping_service.core.domain.Shipment;
import com.felipe.ecommerce_shipping_service.infrastructure.mappers.ShipmentEntityMapper;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipment.ShipmentEntity;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.repositories.ShipmentRepository;
import com.felipe.ecommerce_shipping_service.infrastructure.services.ShipmentSchedulerService;
import org.springframework.stereotype.Component;

@Component
public class ShipmentGatewayImpl implements ShipmentGateway {
  private final ShipmentRepository shipmentRepository;
  private final ShipmentEntityMapper shipmentEntityMapper;
  private final ShipmentSchedulerService shipmentSchedulerService;

  public ShipmentGatewayImpl(ShipmentRepository shipmentRepository, ShipmentEntityMapper shipmentEntityMapper,
                             ShipmentSchedulerService shipmentSchedulerService) {
    this.shipmentRepository = shipmentRepository;
    this.shipmentEntityMapper = shipmentEntityMapper;
    this.shipmentSchedulerService = shipmentSchedulerService;
  }

  @Override
  public Shipment createShipment(Shipment shipment) {
    ShipmentEntity savedEntity = this.shipmentRepository.save(this.shipmentEntityMapper.toEntity(shipment));
    this.shipmentSchedulerService.scheduleShipmentToStatusMutation(savedEntity);
    return this.shipmentEntityMapper.toDomain(savedEntity);
  }
}
