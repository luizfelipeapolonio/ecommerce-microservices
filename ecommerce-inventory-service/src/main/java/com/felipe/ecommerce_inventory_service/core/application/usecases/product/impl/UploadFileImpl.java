package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class UploadFileImpl implements UploadFile {
  private final byte[] content;
  private final String contentType;
  private final String originalFileName;
  private final InputStream inputStream;
  private final long size;

  @Override
  public byte[] getContent() {
    return this.content;
  }

  @Override
  public String getContentType() {
    return this.contentType;
  }

  @Override
  public String getOriginalName() {
    return this.originalFileName;
  }

  @Override
  public InputStream getInputStream() {
    return this.inputStream;
  }

  @Override
  public long getSize() {
    return this.size;
  }

  public static Builder builder() {
    return new Builder();
  }

  private UploadFileImpl(Builder builder) {
    this.content = builder.content;
    this.contentType = builder.contentType;
    this.originalFileName = builder.originalFileName;
    this.inputStream = builder.inputStream;
    this.size = builder.size;
  }

  public static class Builder {
    private byte[] content;
    private String contentType;
    private String originalFileName;
    private InputStream inputStream;
    private long size;

    private Builder() {
    }

    public Builder content(byte[] content) {
      this.content = content;
      return this;
    }

    public Builder contentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder originalFileName(String originalFileName) {
      this.originalFileName = originalFileName;
      return this;
    }

    public Builder inputStream(byte[] content) {
      this.inputStream = new ByteArrayInputStream(content);
      return this;
    }

    public Builder size(long size) {
      this.size = size;
      return this;
    }

    public UploadFileImpl build() {
      return new UploadFileImpl(this);
    }
  }
}
