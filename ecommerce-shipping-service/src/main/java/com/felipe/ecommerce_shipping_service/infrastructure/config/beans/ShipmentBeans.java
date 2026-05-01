package com.felipe.ecommerce_shipping_service.infrastructure.config.beans;

import com.felipe.ecommerce_shipping_service.core.application.gateway.ShipmentGateway;
import com.felipe.ecommerce_shipping_service.core.application.usecases.CreateShipmentUseCase;
import com.felipe.ecommerce_shipping_service.core.application.usecases.impl.CreateShipmentUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ShipmentBeans {
  private final ShipmentGateway shipmentGateway;

  public ShipmentBeans(ShipmentGateway shipmentGateway) {
    this.shipmentGateway = shipmentGateway;
  }

  @Bean
  public CreateShipmentUseCase createShipmentUseCase() {
    return new CreateShipmentUseCaseImpl(this.shipmentGateway);
  }
}
