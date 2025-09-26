package com.felipe.ecommerce_discount_service.infrastructure.persistence.entities;

import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionAppliesTarget;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotion_applies_to")
public class PromotionAppliesToEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 10, nullable = false)
  private String target;

  @Column(nullable = false)
  private String targetId;

  @CreationTimestamp
  @Column(name = "applied_at", nullable = false)
  private LocalDateTime appliedAt;

  @ManyToOne
  @JoinColumn(name = "promotion_id", nullable = false)
  private PromotionEntity promotion;

  public PromotionAppliesToEntity() {
  }

  public Long getId() {
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

  public PromotionEntity getPromotion() {
    return this.promotion;
  }

  public void setPromotion(PromotionEntity promotion) {
    this.promotion = promotion;
  }
}
