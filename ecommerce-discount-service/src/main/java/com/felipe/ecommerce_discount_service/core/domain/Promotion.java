package com.felipe.ecommerce_discount_service.core.domain;

import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionScope;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Promotion {
  private final UUID id;
  private final String name;
  private final String description;
  private final String scope;
  private final String discountType;
  private final String discountValue;
  private final BigDecimal minimumPrice;
  private final LocalDateTime endDate;
  private final boolean isActive;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final List<PromotionAppliesTo> promotionApplies;

  private Promotion(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.description = builder.description;
    this.scope = builder.scope;
    this.discountType = builder.discountType;
    this.discountValue = builder.discountValue;
    this.minimumPrice = builder.minimumPrice;
    this.endDate = builder.endDate;
    this.isActive = builder.isActive;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
    this.promotionApplies = builder.promotionApplies;

    if(this.promotionApplies != null) {
      this.promotionApplies.forEach(promotionAppliesTo -> promotionAppliesTo.setPromotion(this));
    }
  }

  public UUID getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public String getScope() {
    return this.scope;
  }

  public String getDiscountType() {
    return this.discountType;
  }

  public String getDiscountValue() {
    return this.discountValue;
  }

  public BigDecimal getMinimumPrice() {
    return this.minimumPrice;
  }

  public LocalDateTime getEndDate() {
    return this.endDate;
  }

  public boolean isActive() {
    return this.isActive;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public List<PromotionAppliesTo> getPromotionApplies() {
    return this.promotionApplies;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(Promotion promotion) {
    return new Builder(promotion);
  }

  public static class Builder {
    private UUID id;
    private String name;
    private String description;
    private String scope;
    private String discountType;
    private String discountValue;
    private BigDecimal minimumPrice;
    private LocalDateTime endDate;
    private boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PromotionAppliesTo> promotionApplies;

    private Builder() {
    }

    private Builder(Promotion promotion) {
      this.id = promotion.getId();
      this.name = promotion.getName();
      this.description = promotion.getDescription();
      this.scope = promotion.getScope();
      this.discountType = promotion.getDiscountType();
      this.discountValue = promotion.getDiscountValue();
      this.minimumPrice = promotion.getMinimumPrice();
      this.endDate = promotion.getEndDate();
      this.isActive = promotion.isActive();
      this.createdAt = promotion.getCreatedAt();
      this.updatedAt = promotion.getUpdatedAt();
      this.promotionApplies = promotion.getPromotionApplies();
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

    public Builder scope(PromotionScope scope) {
      this.scope = scope.getText();
      return this;
    }

    public Builder discountType(DiscountType discountType) {
      this.discountType = discountType.getText();
      return this;
    }

    public Builder discountValue(String discountValue) {
      this.discountValue = discountValue;
      return this;
    }

    public Builder minimumPrice(BigDecimal minimumPrice) {
      this.minimumPrice = minimumPrice;
      return this;
    }

    public Builder endDate(LocalDateTime endDate) {
      this.endDate = endDate;
      return this;
    }

    public Builder isActive(boolean isActive) {
      this.isActive = isActive;
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

    public Builder promotionApplies(List<PromotionAppliesTo> promotionApplies) {
      this.promotionApplies = promotionApplies;
      return this;
    }

    public Builder addPromotionAppliesTo(PromotionAppliesTo promotionAppliesTo) {
      this.promotionApplies.add(promotionAppliesTo);
      return this;
    }

    public Builder removePromotionAppliesTo(PromotionAppliesTo promotionAppliesTo) {
      this.promotionApplies.remove(promotionAppliesTo);
      return this;
    }

    public Promotion build() {
      return new Promotion(this);
    }
  }
}
