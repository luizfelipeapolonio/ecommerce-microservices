package com.felipe.ecommerce_discount_service.infrastructure.persistence.entities;

import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionScope;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "promotions")
public class PromotionEntity {

  @Id
  private UUID id;

  @Column(length = 150, nullable = false)
  private String name;

  private String description;

  @Column(length = 10, nullable = false)
  private String scope;

  @Column(name = "discount_type", length = 12, nullable = false)
  private String discountType;

  @Column(name = "discount_value", length = 50, nullable = false)
  private String discountValue;

  @Column(name = "minimum_price", nullable = false)
  private BigDecimal minimumPrice;

  @Column(name = "end_date", nullable = false)
  private LocalDateTime endDate;

  @Column(name = "is_active", nullable = false)
  private boolean isActive = true;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<PromotionAppliesToEntity> promotionApplies;

  protected PromotionEntity() {
  }

  private PromotionEntity(Builder builder) {
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
    return this.discountType = discountType;
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

  public List<PromotionAppliesToEntity> getPromotionApplies() {
    return this.promotionApplies;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(PromotionEntity promotion) {
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
    private List<PromotionAppliesToEntity> promotionApplies = new ArrayList<>();

    private Builder() {
    }

    private Builder(PromotionEntity promotion) {
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

    public Builder promotionApplies(List<PromotionAppliesToEntity> promotionApplies) {
      this.promotionApplies = promotionApplies;
      return this;
    }

    public Builder addPromotionAppliesTo(PromotionAppliesToEntity promotionAppliesTo) {
      this.promotionApplies.add(promotionAppliesTo);
      return this;
    }

    public Builder removePromotionAppliesTo(PromotionAppliesToEntity promotionAppliesTo) {
      this.promotionApplies.remove(promotionAppliesTo);
      return this;
    }

    public PromotionEntity build() {
      return new PromotionEntity(this);
    }
  }
}
