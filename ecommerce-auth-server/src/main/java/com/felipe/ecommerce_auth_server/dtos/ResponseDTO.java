package com.felipe.ecommerce_auth_server.dtos;

import com.felipe.ecommerce_auth_server.enums.StatusResponse;
import org.springframework.http.HttpStatus;

public record ResponseDTO(String status, int code, String message) {
  public ResponseDTO(StatusResponse status, HttpStatus code, String message) {
    this(status.getText(), code.value(), message);
  }
}
