package com.felipe.ecommerce_shipping_service.core.application.usecases.impl;

import com.felipe.ecommerce_shipping_service.core.application.dtos.CreateShipmentDTO;
import com.felipe.ecommerce_shipping_service.core.application.gateway.ShipmentGateway;
import com.felipe.ecommerce_shipping_service.core.application.usecases.CreateShipmentUseCase;
import com.felipe.ecommerce_shipping_service.core.application.utils.ShipmentUtils;
import com.felipe.ecommerce_shipping_service.core.domain.Shipment;

public class CreateShipmentUseCaseImpl implements CreateShipmentUseCase {
  private final ShipmentGateway shipmentGateway;

  public CreateShipmentUseCaseImpl(ShipmentGateway shipmentGateway) {
    this.shipmentGateway = shipmentGateway;
  }

  @Override
  public Shipment execute(CreateShipmentDTO shipmentDTO) {
    Shipment shipment = new Shipment()
      .orderId(shipmentDTO.orderId())
      .totalWeight(ShipmentUtils.generateTotalWeight())
      .trackingCode(ShipmentUtils.generateTrackingCode())
      .shipmentAddress(ShipmentUtils.formatShipmentAddress(shipmentDTO.shipmentAddress()));
    return this.shipmentGateway.createShipment(shipment);
  }
}
