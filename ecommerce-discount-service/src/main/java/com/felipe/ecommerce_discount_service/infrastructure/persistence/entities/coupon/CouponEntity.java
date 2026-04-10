package com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon;

import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "coupons")
public class CouponEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 150)
  private String name;

  private String description;

  @Column(name = "coupon_code", nullable = false, unique = true, length = 30)
  private String couponCode;

  @Column(name = "discount_type", nullable = false, length = 12)
  private String discountType;

  @Column(name = "discount_value", nullable = false, length = 50)
  private String discountValue;

  @Column(name = "minimum_price", nullable = false)
  private BigDecimal minimumPrice;

  @Column(name = "is_active", nullable = false)
  private boolean isActive = true;

  @Column(name = "end_date", nullable = false)
  private LocalDateTime endDate;

  @Column(name = "usage_limit", nullable = false)
  private int usageLimit;

  @Column(name = "usage_count", nullable = false)
  private int usageCount;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<CouponAppliedByEntity> appliedBy = new ArrayList<>();

  public CouponEntity() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public CouponEntity id(UUID id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CouponEntity name(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CouponEntity description(String description) {
    this.description = description;
    return this;
  }

  public String getCouponCode() {
    return this.couponCode;
  }

  public void setCouponCode(String couponCode) {
    this.couponCode = couponCode.toUpperCase();
  }

  public CouponEntity couponCode(String couponCode) {
    this.couponCode = couponCode.toUpperCase();
    return this;
  }

  public String getDiscountType() {
    return this.discountType;
  }

  public void setDiscountType(DiscountType discountType) {
    this.discountType = discountType.getText();
  }

  public CouponEntity discountType(DiscountType discountType) {
    this.discountType = discountType.getText();
    return this;
  }

  public String getDiscountValue() {
    return this.discountValue;
  }

  public void setDiscountValue(String discountValue) {
    this.discountValue = discountValue;
  }

  public CouponEntity discountValue(String discountValue) {
    this.discountValue = discountValue;
    return this;
  }

  public BigDecimal getMinimumPrice() {
    return this.minimumPrice;
  }

  public void setMinimumPrice(BigDecimal minimumPrice) {
    this.minimumPrice = minimumPrice;
  }

  public CouponEntity minimumPrice(BigDecimal minimumPrice) {
    this.minimumPrice = minimumPrice;
    return this;
  }

  public boolean isActive() {
    return this.isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public CouponEntity isActive(boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public LocalDateTime getEndDate() {
    return this.endDate;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }

  public CouponEntity endDate(LocalDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  public int getUsageLimit() {
    return this.usageLimit;
  }

  public void setUsageLimit(int usageLimit) {
    this.usageLimit = usageLimit;
  }

  public CouponEntity usageLimit(int usageLimit) {
    this.usageLimit = usageLimit;
    return this;
  }

  public int getUsageCount() {
    return this.usageCount;
  }

  public void setUsageCount(int usageCount) {
    this.usageCount = usageCount;
  }

  public CouponEntity usageCount(int usageCount) {
    this.usageCount = usageCount;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public CouponEntity createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public CouponEntity updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public List<CouponAppliedByEntity> getAppliedBy() {
    return appliedBy;
  }

  public void setAppliedBy(List<CouponAppliedByEntity> appliedBy) {
    this.appliedBy = appliedBy;
  }

  public void addAppliedBy(CouponAppliedByEntity appliedBy) {
    appliedBy.setCoupon(this);
    this.appliedBy.add(appliedBy);
  }
}
