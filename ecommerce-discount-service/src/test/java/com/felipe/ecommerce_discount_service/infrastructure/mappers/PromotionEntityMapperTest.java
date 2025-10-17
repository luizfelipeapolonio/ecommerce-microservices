package com.felipe.ecommerce_discount_service.infrastructure.mappers;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionAppliesToEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PromotionEntityMapperTest {

  @Spy
  private PromotionEntityMapper promotionEntityMapper;

  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }
  
  @Test
  @DisplayName("promotionDomainToEntity - Should successfully convert a Promotion to PromotionEntity")
  void promotionDomainToEntity() {
    final List<PromotionAppliesTo> targetsDomain = this.dataMock.getPromotionAppliesToDomain();
    final Promotion promotionDomain = Promotion.mutate(this.dataMock.getPromotionsDomain().getFirst())
      .promotionApplies(List.of(targetsDomain.get(0), targetsDomain.get(1)))
      .build();
    
    final PromotionEntity convertedPromotion = this.promotionEntityMapper.toEntity(promotionDomain);

    assertThat(convertedPromotion.getId()).isEqualTo(promotionDomain.getId());
    assertThat(convertedPromotion.getName()).isEqualTo(promotionDomain.getName());
    assertThat(convertedPromotion.getDescription()).isEqualTo(promotionDomain.getDescription());
    assertThat(convertedPromotion.getScope()).isEqualTo(promotionDomain.getScope());
    assertThat(convertedPromotion.getDiscountType()).isEqualTo(promotionDomain.getDiscountType());
    assertThat(convertedPromotion.getDiscountValue()).isEqualTo(promotionDomain.getDiscountValue());
    assertThat(convertedPromotion.getMinimumPrice()).isEqualTo(promotionDomain.getMinimumPrice());
    assertThat(convertedPromotion.getEndDate()).isEqualTo(promotionDomain.getEndDate());
    assertThat(convertedPromotion.getCreatedAt()).isEqualTo(promotionDomain.getCreatedAt());
    assertThat(convertedPromotion.getUpdatedAt()).isEqualTo(promotionDomain.getUpdatedAt());
    assertThat(convertedPromotion.getPromotionApplies().get(0)).usingRecursiveComparison().isEqualTo(promotionDomain.getPromotionApplies().get(0));
    assertThat(convertedPromotion.getPromotionApplies().get(1)).usingRecursiveComparison().isEqualTo(promotionDomain.getPromotionApplies().get(1));
  }

  @Test
  @DisplayName("promotionEntityToDomain - Should successfully convert a PromotionEntity to Promotion")
  void promotionEntityToDomain() {
    final List<PromotionAppliesToEntity> targetsEntity = this.dataMock.getPromotionAppliesToEntity();
    final PromotionEntity promotionEntity = PromotionEntity.mutate(this.dataMock.getPromotionsEntity().getFirst())
      .promotionApplies(List.of(targetsEntity.get(0), targetsEntity.get(1)))
      .build();

    final Promotion convertedPromotion = this.promotionEntityMapper.toDomain(promotionEntity);

    assertThat(convertedPromotion.getId()).isEqualTo(promotionEntity.getId());
    assertThat(convertedPromotion.getName()).isEqualTo(promotionEntity.getName());
    assertThat(convertedPromotion.getDescription()).isEqualTo(promotionEntity.getDescription());
    assertThat(convertedPromotion.getScope()).isEqualTo(promotionEntity.getScope());
    assertThat(convertedPromotion.getDiscountType()).isEqualTo(promotionEntity.getDiscountType());
    assertThat(convertedPromotion.getDiscountValue()).isEqualTo(promotionEntity.getDiscountValue());
    assertThat(convertedPromotion.getMinimumPrice()).isEqualTo(promotionEntity.getMinimumPrice());
    assertThat(convertedPromotion.getEndDate()).isEqualTo(promotionEntity.getEndDate());
    assertThat(convertedPromotion.getCreatedAt()).isEqualTo(promotionEntity.getCreatedAt());
    assertThat(convertedPromotion.getUpdatedAt()).isEqualTo(promotionEntity.getUpdatedAt());
    assertThat(convertedPromotion.getPromotionApplies().get(0)).usingRecursiveComparison().isEqualTo(promotionEntity.getPromotionApplies().get(0));
    assertThat(convertedPromotion.getPromotionApplies().get(1)).usingRecursiveComparison().isEqualTo(promotionEntity.getPromotionApplies().get(1));
  }
}
