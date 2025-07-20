package com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "models")
public class ModelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  private String description;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "brand_id", nullable = false)
  private BrandEntity brand;

  protected ModelEntity() {
  }

  public Long getId() {
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

  public BrandEntity getBrand() {
    return this.brand;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(ModelEntity model) {
    return new Builder(model);
  }

  protected ModelEntity(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.description = builder.description;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
    this.brand = builder.brand;
  }

  public static class Builder {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BrandEntity brand;

    private Builder() {
    }

    private Builder(ModelEntity model) {
      this.id = model.getId();
      this.name = model.getName();
      this.description = model.getDescription();
      this.createdAt = model.getCreatedAt();
      this.updatedAt = model.getUpdatedAt();
      this.brand = model.getBrand();
    }

    public Builder id(Long id) {
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

    public Builder brand(BrandEntity brand) {
      this.brand = brand;
      return this;
    }

    public ModelEntity build() {
      return new ModelEntity(this);
    }
  }
}
