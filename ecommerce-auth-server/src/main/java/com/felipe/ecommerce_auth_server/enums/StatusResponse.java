package com.felipe.ecommerce_auth_server.enums;

public enum StatusResponse {
  SUCCESS("success"),
  ERROR("error");

  private final String text;

  StatusResponse(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }
}
