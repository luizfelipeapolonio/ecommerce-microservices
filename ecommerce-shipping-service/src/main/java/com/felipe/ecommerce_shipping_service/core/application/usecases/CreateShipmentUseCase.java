package com.felipe.ecommerce_shipping_service.core.application.usecases;

import com.felipe.ecommerce_shipping_service.core.application.dtos.CreateShipmentDTO;
import com.felipe.ecommerce_shipping_service.core.domain.Shipment;

public interface CreateShipmentUseCase {
  Shipment execute(CreateShipmentDTO shipmentDTO);
}
