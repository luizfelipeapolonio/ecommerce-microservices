package com.felipe.ecommerce_shipping_service.infrastructure.mappers;

import com.felipe.ecommerce_shipping_service.core.domain.Shipment;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipment.ShipmentEntity;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEntityMapper {
  public ShipmentEntity toEntity(Shipment shipment) {
    return new ShipmentEntity()
      .id(shipment.getId())
      .orderId(shipment.getOrderId())
      .status(shipment.getStatus())
      .trackingCode(shipment.getTrackingCode())
      .totalWeight(shipment.getTotalWeight())
      .shippedAt(shipment.getShippedAt())
      .createdAt(shipment.getCreatedAt())
      .updatedAt(shipment.getUpdatedAt())
      .deliveredAt(shipment.getDeliveredAt())
      .shipmentAddress(shipment.getShipmentAddress());
  }

  public Shipment toDomain(ShipmentEntity entity) {
    return new Shipment()
      .id(entity.getId())
      .orderId(entity.getOrderId())
      .status(entity.getStatus())
      .trackingCode(entity.getTrackingCode())
      .totalWeight(entity.getTotalWeight())
      .shippedAt(entity.getShippedAt())
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt())
      .deliveredAt(entity.getDeliveredAt())
      .shipmentAddress(entity.getShipmentAddress());
  }
}
