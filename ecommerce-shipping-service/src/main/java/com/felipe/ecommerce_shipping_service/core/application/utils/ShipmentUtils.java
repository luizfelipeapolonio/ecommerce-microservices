package com.felipe.ecommerce_shipping_service.core.application.utils;

import com.felipe.ecommerce_shipping_service.core.application.dtos.CreateShipmentDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class ShipmentUtils {
  private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final String TRACKING_CODE_PREFIX = "BR";
  private static final int TRACKING_CODE_LENGTH = 10;

  public static String generateTrackingCode() {
    final String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    ThreadLocalRandom random = ThreadLocalRandom.current();
    StringBuilder code = new StringBuilder(TRACKING_CODE_LENGTH);

    for (int i = 0; i < TRACKING_CODE_LENGTH; i++) {
      char letter = ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length()));
      code.append(letter);
    }
    return TRACKING_CODE_PREFIX + "-" + date + "-" + code;
  }

  public static String generateTotalWeight() {
    double weight = ThreadLocalRandom.current().nextDouble(1.0, 50.0);
    return BigDecimal.valueOf(weight)
      .setScale(3, RoundingMode.HALF_UP)
      .toPlainString()
      .concat(" Kg");
  }

  public static String formatShipmentAddress(CreateShipmentDTO.ShipmentAddress shipmentAddress) {
    return shipmentAddress.street() + ", " + shipmentAddress.number() + " - " +
      "Complemento: " + shipmentAddress.complement() + " - " +
      "Bairro: " + shipmentAddress.district() + " - " +
      "CEP: " + shipmentAddress.zipcode() + " - " +
      "Cidade: " +  shipmentAddress.city() + " - " +
      "Estado: " + shipmentAddress.state() + " - " +
      "País: " + shipmentAddress.country();
  }
}
