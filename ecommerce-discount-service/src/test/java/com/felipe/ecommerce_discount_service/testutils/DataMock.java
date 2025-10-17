package com.felipe.ecommerce_discount_service.testutils;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionAppliesTarget;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionScope;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionAppliesToEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataMock {
  private final List<Promotion> promotionsDomain = new ArrayList<>();
  private final List<PromotionAppliesTo> promotionAppliesToDomain = new ArrayList<>();
  private final List<PromotionEntity> promotionsEntity = new ArrayList<>();
  private final List<PromotionAppliesToEntity> promotionAppliesToEntity = new ArrayList<>();

  public DataMock() {
    this.createPromotionsDomain();
    this.createPromotionAppliesToDomain();
    this.createPromotionsEntity();
    this.createPromotionAppliesToEntity();
  }

  public List<Promotion> getPromotionsDomain() {
    return this.promotionsDomain;
  }

  public List<PromotionEntity> getPromotionsEntity() {
    return this.promotionsEntity;
  }

  public List<PromotionAppliesTo> getPromotionAppliesToDomain() {
    return this.promotionAppliesToDomain;
  }

  public List<PromotionAppliesToEntity> getPromotionAppliesToEntity() {
    return this.promotionAppliesToEntity;
  }

  private void createPromotionsDomain() {
    Promotion promotion1 = Promotion.builder()
      .id(UUID.fromString("d37032fc-a73d-4ad6-bbec-d7078d70a394"))
      .name("Promotion 1")
      .description("Description of Promotion 1")
      .discountType(DiscountType.FIXED_AMOUNT)
      .discountValue("20.00")
      .minimumPrice(new BigDecimal("20.00"))
      .scope(PromotionScope.ALL)
      .createdAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"))
      .updatedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"))
      .endDate(LocalDateTime.now().plusDays(10))
      .build();

    this.promotionsDomain.add(promotion1);
  }

  private void createPromotionAppliesToDomain() {
    PromotionAppliesTo target1 = new PromotionAppliesTo();
    target1.setId(1L);
    target1.setTarget(PromotionAppliesTarget.CATEGORY);
    target1.setTargetId("1");
    target1.setAppliedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"));

    PromotionAppliesTo target2 = new PromotionAppliesTo();
    target2.setId(2L);
    target2.setTarget(PromotionAppliesTarget.BRAND);
    target2.setTargetId("1");
    target2.setAppliedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"));

    PromotionAppliesTo target3 = new PromotionAppliesTo();
    target3.setId(3L);
    target3.setTarget(PromotionAppliesTarget.MODEL);
    target3.setTargetId("1");
    target3.setAppliedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"));

    PromotionAppliesTo target4 = new PromotionAppliesTo();
    target4.setId(4L);
    target4.setTarget(PromotionAppliesTarget.PRODUCT);
    target4.setTargetId("a1cb86bf-817f-4a95-8f82-b1f215a0ee3c");
    target4.setAppliedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"));

    this.promotionAppliesToDomain.add(target1);
    this.promotionAppliesToDomain.add(target2);
    this.promotionAppliesToDomain.add(target3);
    this.promotionAppliesToDomain.add(target4);
  }

  private void createPromotionsEntity() {
    PromotionEntity promotion1 = PromotionEntity.builder()
      .id(UUID.fromString("d37032fc-a73d-4ad6-bbec-d7078d70a394"))
      .name("Promotion 1")
      .description("Description of Promotion 1")
      .discountType(DiscountType.FIXED_AMOUNT)
      .discountValue("20.00")
      .minimumPrice(new BigDecimal("20.00"))
      .scope(PromotionScope.ALL)
      .createdAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"))
      .updatedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"))
      .endDate(LocalDateTime.now().plusDays(10))
      .build();

    this.promotionsEntity.add(promotion1);
  }

  private void createPromotionAppliesToEntity() {
    PromotionAppliesToEntity target1 = new PromotionAppliesToEntity();
    target1.setId(1L);
    target1.setTarget(PromotionAppliesTarget.CATEGORY);
    target1.setTargetId("1");
    target1.setAppliedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"));

    PromotionAppliesToEntity target2 = new PromotionAppliesToEntity();
    target2.setId(2L);
    target2.setTarget(PromotionAppliesTarget.BRAND);
    target2.setTargetId("1");
    target2.setAppliedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"));

    PromotionAppliesToEntity target3 = new PromotionAppliesToEntity();
    target3.setId(3L);
    target3.setTarget(PromotionAppliesTarget.MODEL);
    target3.setTargetId("1");
    target3.setAppliedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"));

    PromotionAppliesToEntity target4 = new PromotionAppliesToEntity();
    target4.setId(4L);
    target4.setTarget(PromotionAppliesTarget.PRODUCT);
    target4.setTargetId("a1cb86bf-817f-4a95-8f82-b1f215a0ee3c");
    target4.setAppliedAt(LocalDateTime.parse("2025-10-14T17:55:30.467863282"));

    this.promotionAppliesToEntity.add(target1);
    this.promotionAppliesToEntity.add(target2);
    this.promotionAppliesToEntity.add(target3);
    this.promotionAppliesToEntity.add(target4);
  }
}
