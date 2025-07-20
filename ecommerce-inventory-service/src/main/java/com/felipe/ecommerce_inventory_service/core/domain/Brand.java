package com.felipe.ecommerce_inventory_service.core.domain;

import java.time.LocalDateTime;

public class Brand {
  private final long id;
  private final String name;
  private final String description;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(Brand brand) {
    return new Builder(brand);
  }

  private Brand(Builder builder) {
    this.id =  builder.id;
    this.name = builder.name;
    this.description = builder.description;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
  }

  public static class Builder {
    private long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Builder() {
    }

    private Builder(Brand brand) {
      this.id =  brand.getId();
      this.name = brand.getName();
      this.description = brand.getDescription();
      this.createdAt = brand.getCreatedAt();
      this.updatedAt = brand.getUpdatedAt();
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Brand build() {
      return new Brand(this);
    }
  }
}
