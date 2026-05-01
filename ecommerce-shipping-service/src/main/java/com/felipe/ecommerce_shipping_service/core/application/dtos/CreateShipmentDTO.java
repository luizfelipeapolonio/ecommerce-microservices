package com.felipe.ecommerce_shipping_service.core.application.dtos;

import java.util.UUID;

public record CreateShipmentDTO(UUID orderId, ShipmentAddress shipmentAddress) {
  public record ShipmentAddress(
    String street,
    String number,
    String complement,
    String district,
    String zipcode,
    String city,
    String state,
    String country
  ) {}
}
