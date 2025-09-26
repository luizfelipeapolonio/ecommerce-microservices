package com.felipe.ecommerce_discount_service.core.domain;

import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionAppliesTarget;

import java.time.LocalDateTime;

public class PromotionAppliesTo {
  private long id;
  private String target;
  private String targetId;
  private LocalDateTime appliedAt;
  private Promotion promotion;

  public PromotionAppliesTo() {
  }

  public long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTarget() {
    return this.target;
  }

  public void setTarget(PromotionAppliesTarget target) {
    this.target = target.getText();
  }

  public String getTargetId() {
    return this.targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public LocalDateTime getAppliedAt() {
    return this.appliedAt;
  }

  public void setAppliedAt(LocalDateTime appliedAt) {
    this.appliedAt = appliedAt;
  }

  public Promotion getPromotion() {
    return this.promotion;
  }

  public void setPromotion(Promotion promotion) {
    this.promotion = promotion;
  }
}
