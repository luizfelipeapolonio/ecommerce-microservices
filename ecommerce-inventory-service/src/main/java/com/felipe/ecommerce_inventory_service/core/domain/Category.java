package com.felipe.ecommerce_inventory_service.core.domain;

import java.time.LocalDateTime;

public class Category {
  private final long id;
  private final String name;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Category parentCategory;

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public Category getParentCategory() {
    return this.parentCategory;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(Category category) {
    return new Builder(category);
  }

  private Category(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
    this.parentCategory = builder.parentCategory;
  }

  public static class Builder {
    private long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Category parentCategory;

    private Builder() {
    }

    private Builder(Category category) {
      this.id = category.getId();
      this.name = category.getName();
      this.createdAt = category.getCreatedAt();
      this.updatedAt = category.getUpdatedAt();
      this.parentCategory = category.getParentCategory();
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
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

    public Builder parentCategory(Category parentCategory) {
      this.parentCategory = parentCategory;
      return this;
    }

    public Category build() {
      return new Category(this);
    }
  }
}
