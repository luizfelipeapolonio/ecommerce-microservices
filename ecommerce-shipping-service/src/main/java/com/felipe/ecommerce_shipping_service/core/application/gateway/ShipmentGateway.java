package com.felipe.ecommerce_shipping_service.core.application.gateway;

import com.felipe.ecommerce_shipping_service.core.domain.Shipment;

public interface ShipmentGateway {
  Shipment createShipment(Shipment shipment);
}
