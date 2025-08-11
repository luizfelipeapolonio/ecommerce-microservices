package com.felipe.ecommerce_upload_service.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ImageUtils {
  // Using International System of Units (SI)
  private static final double UNIT_BYTE_IN_MEGABYTES = Math.pow(1000, -2);
  private static final int UNIT_KILOBYTE_IN_BYTES = 1000;
  private static final int UNIT_MEGABYTE_IN_BYTES = 1000000;

  public static String convertBytes(long bytes) {
    String unitAcronym = "B";
    String convertedValue = String.valueOf(bytes);

    if(bytes >= UNIT_KILOBYTE_IN_BYTES && bytes < UNIT_MEGABYTE_IN_BYTES) {
      unitAcronym = "KB";
      double kilobytes = (double) bytes / UNIT_KILOBYTE_IN_BYTES;
      convertedValue = new BigDecimal(kilobytes).setScale(2, RoundingMode.HALF_UP).toString();
    }

    if(bytes >= UNIT_MEGABYTE_IN_BYTES) {
      unitAcronym = "MB";
      double megabytes = bytes * UNIT_BYTE_IN_MEGABYTES;
      convertedValue = new BigDecimal(megabytes).setScale(2, RoundingMode.HALF_UP).toString();
    }

    return String.format("%s %s", convertedValue, unitAcronym);
  }
}
