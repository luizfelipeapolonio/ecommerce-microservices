package com.felipe.ecommerce_inventory_service.core.domain;

import java.time.LocalDateTime;

public class Model {
  private final long id;
  private final String name;
  private final String description;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Brand brand;

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

  public Brand getBrand() {
    return this.brand;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(Model model) {
    return new Builder(model);
  }

  private Model(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.description =builder.description;
    this.createdAt = builder.createdAt;
    this.updatedAt =builder.updatedAt;
    this.brand = builder.brand;
  }

  public static class Builder {
    private long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Brand brand;

    private Builder() {
    }

    private Builder(Model model) {
      this.id = model.getId();
      this.name = model.getName();
      this.description = model.getDescription();
      this.createdAt = model.getCreatedAt();
      this.updatedAt = model.getUpdatedAt();
      this.brand = model.getBrand();
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

    public Builder brand(Brand brand) {
      this.brand = brand;
      return this;
    }

    public Model build() {
      return new Model(this);
    }
  }
}
