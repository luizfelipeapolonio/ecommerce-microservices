package com.felipe.ecommerce_inventory_service.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Product {
  private final UUID id;
  private final String name;
  private final String description;
  private final BigDecimal unitPrice;
  private final long quantity;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Category category;
  private final Brand brand;
  private final Model model;

  public UUID getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public BigDecimal getUnitPrice() {
    return this.unitPrice;
  }

  public long getQuantity() {
    return this.quantity;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public Category getCategory() {
    return this.category;
  }

  public Brand getBrand() {
    return this.brand;
  }

  public Model getModel() {
    return this.model;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(Product product) {
    return new Builder(product);
  }

  private Product(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.description =  builder.description;
    this.unitPrice = builder.unitPrice;
    this.quantity = builder.quantity;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
    this.category = builder.category;
    this.brand = builder.brand;
    this.model = builder.model;
  }

  public static class Builder {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private long quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Category category;
    private Brand brand;
    private Model model;

    private Builder() {
    }

    private Builder(Product product) {
      this.id = product.getId();
      this.name = product.getName();
      this.description = product.getDescription();
      this.unitPrice = product.getUnitPrice();
      this.quantity = product.getQuantity();
      this.createdAt = product.getCreatedAt();
      this.updatedAt = product.getUpdatedAt();
      this.category = product.getCategory();
      this.brand = product.getBrand();
      this.model = product.getModel();
    }

    public Builder id(UUID id) {
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

    public Builder unitPrice(BigDecimal unitPrice) {
      this.unitPrice = unitPrice;
      return this;
    }

    public Builder quantity(long quantity) {
      this.quantity = quantity;
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

    public Builder category(Category category) {
      this.category = category;
      return this;
    }

    public Builder brand(Brand brand) {
      this.brand = brand;
      return this;
    }

    public Builder model(Model model) {
      this.model = model;
      return this;
    }

    public Product build() {
      return new Product(this);
    }
  }
}
