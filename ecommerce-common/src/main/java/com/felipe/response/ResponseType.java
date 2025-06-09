package com.felipe.response;

public enum ResponseType {
  SUCCESS("success"),
  ERROR("error");

  private final String text;

  ResponseType(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }
}
