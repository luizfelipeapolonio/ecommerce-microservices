package com.felipe.ecommerce_discount_service.infrastructure.gateway;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.infrastructure.external.InventoryService;
import com.felipe.ecommerce_discount_service.infrastructure.mappers.PromotionEntityMapper;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionAppliesToEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories.PromotionRepository;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PromotionGatewayImplTest {

  @Mock
  private PromotionRepository promotionRepository;

  @Mock
  private PromotionEntityMapper promotionEntityMapper;

  @Mock
  private InventoryService inventoryService;

  @InjectMocks
  private PromotionGatewayImpl promotionGateway;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createPromotionSuccess - Should successfully create a promotion and return an Optional of Promotion")
  void createPromotionSuccess() {
    final List<PromotionAppliesTo> targetsDomain = this.dataMock.getPromotionAppliesToDomain();
    final List<PromotionAppliesToEntity> targetsEntity = this.dataMock.getPromotionAppliesToEntity();

    final Promotion promotionDomain = Promotion.mutate(this.dataMock.getPromotionsDomain().getFirst())
      .promotionApplies(List.of(targetsDomain.get(0), targetsDomain.get(1)))
      .build();
    final PromotionEntity promotionEntity = PromotionEntity.mutate(this.dataMock.getPromotionsEntity().getFirst())
      .promotionApplies(List.of(targetsEntity.get(0), targetsEntity.get(1)))
      .build();

    final ResponsePayload<Map<String, Integer>> inventoryServiceResponse = new ResponsePayload.Builder<Map<String, Integer>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Promoção aplicada com sucesso em 12 produto(s)")
      .payload(Map.of("appliedPromotionQuantity", 12))
      .build();

    when(this.promotionEntityMapper.toEntity(promotionDomain)).thenReturn(promotionEntity);
    when(this.inventoryService.applyPromotion(any(InventoryService.PromotionRequest.class))).thenReturn(inventoryServiceResponse);
    when(this.promotionRepository.save(promotionEntity)).thenReturn(promotionEntity);
    when(this.promotionEntityMapper.toDomain(promotionEntity)).thenReturn(promotionDomain);

    final Optional<Promotion> createdPromotion = this.promotionGateway.createPromotion(promotionDomain);

    assertThat(createdPromotion.isPresent()).isTrue();
    assertThat(createdPromotion.get().getId()).isEqualTo(promotionDomain.getId());
    assertThat(createdPromotion.get().getName()).isEqualTo(promotionDomain.getName());
    assertThat(createdPromotion.get().getDescription()).isEqualTo(promotionDomain.getDescription());
    assertThat(createdPromotion.get().getScope()).isEqualTo(promotionDomain.getScope());
    assertThat(createdPromotion.get().getDiscountType()).isEqualTo(promotionDomain.getDiscountType());
    assertThat(createdPromotion.get().getDiscountValue()).isEqualTo(promotionDomain.getDiscountValue());
    assertThat(createdPromotion.get().getMinimumPrice()).isEqualTo(promotionDomain.getMinimumPrice());
    assertThat(createdPromotion.get().getEndDate()).isEqualTo(promotionDomain.getEndDate());
    assertThat(createdPromotion.get().getCreatedAt()).isEqualTo(promotionDomain.getCreatedAt());
    assertThat(createdPromotion.get().getUpdatedAt()).isEqualTo(promotionDomain.getUpdatedAt());
    assertThat(createdPromotion.get().getPromotionApplies().get(0)).usingRecursiveComparison().isEqualTo(promotionDomain.getPromotionApplies().get(0));
    assertThat(createdPromotion.get().getPromotionApplies().get(1)).usingRecursiveComparison().isEqualTo(promotionDomain.getPromotionApplies().get(1));

    verify(this.promotionEntityMapper, times(1)).toEntity(promotionDomain);
    verify(this.inventoryService, times(1)).applyPromotion(any(InventoryService.PromotionRequest.class));
    verify(this.promotionRepository, times(1)).save(promotionEntity);
    verify(this.promotionEntityMapper, times(1)).toDomain(promotionEntity);
  }

  @Test
  @DisplayName("createPromotionReturnsOptionalEmpty - Should return an optional empty if the promotion is not applied to any product")
  void createPromotionReturnsOptionalEmpty() {
    final List<PromotionAppliesTo> targetsDomain = this.dataMock.getPromotionAppliesToDomain();
    final List<PromotionAppliesToEntity> targetsEntity = this.dataMock.getPromotionAppliesToEntity();

    final Promotion promotionDomain = Promotion.mutate(this.dataMock.getPromotionsDomain().getFirst())
      .promotionApplies(List.of(targetsDomain.get(0), targetsDomain.get(1)))
      .build();
    final PromotionEntity promotionEntity = PromotionEntity.mutate(this.dataMock.getPromotionsEntity().getFirst())
      .promotionApplies(List.of(targetsEntity.get(0), targetsEntity.get(1)))
      .build();

    final ResponsePayload<Map<String, Integer>> inventoryServiceResponse = new ResponsePayload.Builder<Map<String, Integer>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Promoção aplicada com sucesso em 0 produto(s)")
      .payload(Map.of("appliedPromotionQuantity", 0))
      .build();

    when(this.promotionEntityMapper.toEntity(promotionDomain)).thenReturn(promotionEntity);
    when(this.inventoryService.applyPromotion(any(InventoryService.PromotionRequest.class))).thenReturn(inventoryServiceResponse);

    final Optional<Promotion> createdPromotion = this.promotionGateway.createPromotion(promotionDomain);

    assertThat(createdPromotion.isEmpty()).isTrue();
    verify(this.promotionEntityMapper, times(1)).toEntity(promotionDomain);
    verify(this.inventoryService, times(1)).applyPromotion(any(InventoryService.PromotionRequest.class));
    verify(this.promotionRepository, never()).save(any(PromotionEntity.class));
    verify(this.promotionEntityMapper, never()).toDomain(any(PromotionEntity.class));
  }
}
