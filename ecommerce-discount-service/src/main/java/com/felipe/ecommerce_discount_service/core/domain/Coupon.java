package com.felipe.ecommerce_discount_service.core.domain;

import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Coupon {
  private UUID id;
  private String name;
  private String description;
  private String couponCode;
  private String discountType;
  private String discountValue;
  private BigDecimal minimumPrice;
  private boolean isActive = true;
  private LocalDateTime endDate;
  private int usageLimit;
  private int usageCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<CouponAppliedBy> appliedBy = new ArrayList<>();

  public Coupon() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Coupon id(UUID id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Coupon name(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Coupon description(String description) {
    this.description = description;
    return this;
  }

  public String getCouponCode() {
    return this.couponCode;
  }

  public void setCouponCode(String couponCode) {
    this.couponCode = couponCode.toUpperCase();
  }

  public Coupon couponCode(String couponCode) {
    this.couponCode = couponCode.toUpperCase();
    return this;
  }

  public String getDiscountType() {
    return this.discountType;
  }

  public void setDiscountType(DiscountType discountType) {
    this.discountType = discountType.getText();
  }

  public Coupon discountType(DiscountType discountType) {
    this.discountType = discountType.getText();
    return this;
  }

  public String getDiscountValue() {
    return this.discountValue;
  }

  public void setDiscountValue(String discountValue) {
    this.discountValue = discountValue;
  }

  public Coupon discountValue(String discountValue) {
    this.discountValue = discountValue;
    return this;
  }

  public BigDecimal getMinimumPrice() {
    return this.minimumPrice;
  }

  public void setMinimumPrice(BigDecimal minimumPrice) {
    this.minimumPrice = minimumPrice;
  }

  public Coupon minimumPrice(BigDecimal minimumPrice) {
    this.minimumPrice = minimumPrice;
    return this;
  }

  public boolean isActive() {
    return this.isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public Coupon isActive(boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public LocalDateTime getEndDate() {
    return this.endDate;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }

  public Coupon endDate(LocalDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  public int getUsageLimit() {
    return this.usageLimit;
  }

  public void setUsageLimit(int usageLimit) {
    this.usageLimit = usageLimit;
  }

  public Coupon usageLimit(int usageLimit) {
    this.usageLimit = usageLimit;
    return this;
  }

  public int getUsageCount() {
    return this.usageCount;
  }

  public void setUsageCount(int usageCount) {
    this.usageCount = usageCount;
  }

  public Coupon usageCount(int usageCount) {
    this.usageCount = usageCount;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Coupon createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Coupon updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public List<CouponAppliedBy> getAppliedBy() {
    return this.appliedBy;
  }

  public void setAppliedBy(List<CouponAppliedBy> appliedBy) {
    this.appliedBy = appliedBy;
  }

  public void addAppliedBy(CouponAppliedBy appliedBy) {
    appliedBy.setCoupon(this);
    this.appliedBy.add(appliedBy);
  }

  public void removeAppliedBy(CouponAppliedBy appliedBy) {
    appliedBy.setCoupon(null);
    this.appliedBy.remove(appliedBy);
  }

  public void increaseUsageCount() {
    this.usageCount += 1;
  }

  public void decreaseUsageCount() {
    this.usageCount -= 1;
  }
}
