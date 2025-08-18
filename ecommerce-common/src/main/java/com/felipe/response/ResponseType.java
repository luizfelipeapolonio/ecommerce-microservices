package com.felipe.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseType {
  SUCCESS("success"),
  ERROR("error");

  private final String text;

  ResponseType(String text) {
    this.text = text;
  }

  @JsonValue
  public String getText() {
    return this.text;
  }
}
