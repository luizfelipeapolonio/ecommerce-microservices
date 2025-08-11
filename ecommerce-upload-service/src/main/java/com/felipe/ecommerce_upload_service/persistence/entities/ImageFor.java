package com.felipe.ecommerce_upload_service.persistence.entities;

public enum ImageFor {
  THUMBNAIL("thumbnail"),
  SHOWCASE("showcase");

  private final String text;

  ImageFor(String text) {
    this.text = text;
  }

  public String text() {
    return this.text;
  }
}
