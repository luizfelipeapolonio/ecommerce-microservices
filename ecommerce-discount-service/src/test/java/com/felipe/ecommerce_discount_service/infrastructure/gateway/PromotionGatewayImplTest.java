package com.felipe.ecommerce_discount_service.infrastructure.gateway;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.infrastructure.external.InventoryService;
import com.felipe.ecommerce_discount_service.infrastructure.mappers.PromotionEntityMapper;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionAppliesToEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories.PromotionRepository;
import com.felipe.ecommerce_discount_service.infrastructure.services.PromotionSchedulerService;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import com.felipe.kafka.ExpiredPromotionKafkaDTO;
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
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PromotionGatewayImplTest {

  @Mock
  private PromotionRepository promotionRepository;

  @Mock
  private PromotionEntityMapper promotionEntityMapper;

  @Mock
  private InventoryService inventoryService;

  @Mock
  private PromotionSchedulerService promotionSchedulerService;

  @Mock
  private KafkaTemplate<String, ExpiredPromotionKafkaDTO> kafkaTemplate;

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

    when(this.promotionEntityMapper.toEntity(any(Promotion.class))).thenReturn(promotionEntity);
    when(this.inventoryService.applyPromotion(any(InventoryService.PromotionRequest.class))).thenReturn(inventoryServiceResponse);
    when(this.promotionRepository.save(promotionEntity)).thenReturn(promotionEntity);
    doNothing().when(this.promotionSchedulerService).schedulePromotionToExpire(promotionEntity);
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

    verify(this.promotionEntityMapper, times(1)).toEntity(any(Promotion.class));
    verify(this.inventoryService, times(1)).applyPromotion(any(InventoryService.PromotionRequest.class));
    verify(this.promotionRepository, times(1)).save(promotionEntity);
    verify(this.promotionSchedulerService, times(1)).schedulePromotionToExpire(promotionEntity);
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

    when(this.promotionEntityMapper.toEntity(any(Promotion.class))).thenReturn(promotionEntity);
    when(this.inventoryService.applyPromotion(any(InventoryService.PromotionRequest.class))).thenReturn(inventoryServiceResponse);

    final Optional<Promotion> createdPromotion = this.promotionGateway.createPromotion(promotionDomain);

    assertThat(createdPromotion.isEmpty()).isTrue();
    verify(this.promotionEntityMapper, times(1)).toEntity(any(Promotion.class));
    verify(this.inventoryService, times(1)).applyPromotion(any(InventoryService.PromotionRequest.class));
    verify(this.promotionRepository, never()).save(any(PromotionEntity.class));
    verify(this.promotionSchedulerService, never()).schedulePromotionToExpire(any(PromotionEntity.class));
    verify(this.promotionEntityMapper, never()).toDomain(any(PromotionEntity.class));
  }

  @Test
  @DisplayName("findPromotionByIdSuccess - Should successfully find a promotion and return an Optional of Promotion")
  void findPromotionByIdReturnsOptionalOfPromotion() {
    final Promotion promotionDomain = this.dataMock.getPromotionsDomain().getFirst();
    final PromotionEntity promotionEntity = this.dataMock.getPromotionsEntity().getFirst();

    when(this.promotionRepository.findById(promotionDomain.getId())).thenReturn(Optional.of(promotionEntity));
    when(this.promotionEntityMapper.toDomain(promotionEntity)).thenReturn(promotionDomain);

    Optional<Promotion> foundPromotion = this.promotionGateway.findPromotionById(promotionDomain.getId());

    assertThat(foundPromotion.isPresent()).isTrue();
    assertThat(foundPromotion.get().getId()).isEqualTo(promotionDomain.getId());
    assertThat(foundPromotion.get().getName()).isEqualTo(promotionDomain.getName());
    assertThat(foundPromotion.get().getDescription()).isEqualTo(promotionDomain.getDescription());
    assertThat(foundPromotion.get().isActive()).isEqualTo(promotionDomain.isActive());
    assertThat(foundPromotion.get().getScope()).isEqualTo(promotionDomain.getScope());
    assertThat(foundPromotion.get().getMinimumPrice()).isEqualTo(promotionDomain.getMinimumPrice());
    assertThat(foundPromotion.get().getDiscountType()).isEqualTo(promotionDomain.getDiscountType());
    assertThat(foundPromotion.get().getDiscountValue()).isEqualTo(promotionDomain.getDiscountValue());
    assertThat(foundPromotion.get().getEndDate()).isEqualTo(promotionDomain.getEndDate());
    assertThat(foundPromotion.get().getCreatedAt()).isEqualTo(promotionDomain.getCreatedAt());
    assertThat(foundPromotion.get().getUpdatedAt()).isEqualTo(promotionDomain.getUpdatedAt());
    assertThat(foundPromotion.get().getPromotionApplies().size()).isEqualTo(promotionDomain.getPromotionApplies().size());

    verify(this.promotionRepository, times(1)).findById(promotionDomain.getId());
    verify(this.promotionEntityMapper, times(1)).toDomain(promotionEntity);
  }

  @Test
  @DisplayName("findPromotionByIdReturnsOptionalEmpty - Should return an Optional empty")
  void findPromotionByIdReturnsOptionalEmpty() {
    final UUID promotionId = this.dataMock.getPromotionsDomain().getFirst().getId();

    when(this.promotionRepository.findById(promotionId)).thenReturn(Optional.empty());

    Optional<Promotion> foundPromotion = this.promotionGateway.findPromotionById(promotionId);

    assertThat(foundPromotion.isEmpty()).isTrue();
    verify(this.promotionRepository, times(1)).findById(promotionId);
    verify(this.promotionEntityMapper, never()).toDomain(any(PromotionEntity.class));
  }

  @Test
  @DisplayName("deletePromotionSuccessWithIsActiveTrue - Should successfully delete a promotion and return the deleted promotion")
  void deletePromotionSuccessWithIsActiveTrue() {
    final Promotion promotionDomain = this.dataMock.getPromotionsDomain().getFirst();
    final PromotionEntity promotionEntity = this.dataMock.getPromotionsEntity().getFirst();

    when(this.promotionEntityMapper.toEntity(promotionDomain)).thenReturn(promotionEntity);
    doNothing().when(this.promotionRepository).delete(promotionEntity);
    doNothing().when(this.promotionSchedulerService).cancelScheduledPromotion(promotionEntity);
    when(this.kafkaTemplate.send(eq("expired-promotion"), any(ExpiredPromotionKafkaDTO.class))).thenReturn(any());

    Promotion deletedPromotion = this.promotionGateway.deletePromotion(promotionDomain);

    assertThat(deletedPromotion.getId()).isEqualTo(promotionDomain.getId());
    assertThat(deletedPromotion.getName()).isEqualTo(promotionDomain.getName());
    assertThat(deletedPromotion.getDescription()).isEqualTo(promotionDomain.getDescription());
    assertThat(deletedPromotion.isActive()).isEqualTo(promotionDomain.isActive());
    assertThat(deletedPromotion.getScope()).isEqualTo(promotionDomain.getScope());
    assertThat(deletedPromotion.getMinimumPrice()).isEqualTo(promotionDomain.getMinimumPrice());
    assertThat(deletedPromotion.getDiscountType()).isEqualTo(promotionDomain.getDiscountType());
    assertThat(deletedPromotion.getDiscountValue()).isEqualTo(promotionDomain.getDiscountValue());
    assertThat(deletedPromotion.getEndDate()).isEqualTo(promotionDomain.getEndDate());
    assertThat(deletedPromotion.getCreatedAt()).isEqualTo(promotionDomain.getCreatedAt());
    assertThat(deletedPromotion.getUpdatedAt()).isEqualTo(promotionDomain.getUpdatedAt());
    assertThat(deletedPromotion.getPromotionApplies().size()).isEqualTo(promotionDomain.getPromotionApplies().size());

    verify(this.promotionEntityMapper, times(1)).toEntity(promotionDomain);
    verify(this.promotionRepository, times(1)).delete(promotionEntity);
    verify(this.promotionSchedulerService, times(1)).cancelScheduledPromotion(promotionEntity);
    verify(this.kafkaTemplate, times(1)).send(eq("expired-promotion"), any(ExpiredPromotionKafkaDTO.class));
  }

  @Test
  @DisplayName("deletePromotionSuccessWithIsActiveFalse - Should successfully delete a promotion and return the deleted promotion")
  void deletePromotionSuccessWithIsActiveFalse() {
    final Promotion promotionDomain = Promotion.mutate(this.dataMock.getPromotionsDomain().getFirst())
      .isActive(false)
      .build();
    final PromotionEntity promotionEntity = this.dataMock.getPromotionsEntity().getFirst();

    when(this.promotionEntityMapper.toEntity(promotionDomain)).thenReturn(promotionEntity);
    doNothing().when(this.promotionRepository).delete(promotionEntity);
    doNothing().when(this.promotionSchedulerService).cancelScheduledPromotion(promotionEntity);

    Promotion deletedPromotion = this.promotionGateway.deletePromotion(promotionDomain);

    assertThat(deletedPromotion.getId()).isEqualTo(promotionDomain.getId());
    assertThat(deletedPromotion.getName()).isEqualTo(promotionDomain.getName());
    assertThat(deletedPromotion.getDescription()).isEqualTo(promotionDomain.getDescription());
    assertThat(deletedPromotion.isActive()).isEqualTo(promotionDomain.isActive());
    assertThat(deletedPromotion.getScope()).isEqualTo(promotionDomain.getScope());
    assertThat(deletedPromotion.getMinimumPrice()).isEqualTo(promotionDomain.getMinimumPrice());
    assertThat(deletedPromotion.getDiscountType()).isEqualTo(promotionDomain.getDiscountType());
    assertThat(deletedPromotion.getDiscountValue()).isEqualTo(promotionDomain.getDiscountValue());
    assertThat(deletedPromotion.getEndDate()).isEqualTo(promotionDomain.getEndDate());
    assertThat(deletedPromotion.getCreatedAt()).isEqualTo(promotionDomain.getCreatedAt());
    assertThat(deletedPromotion.getUpdatedAt()).isEqualTo(promotionDomain.getUpdatedAt());
    assertThat(deletedPromotion.getPromotionApplies().size()).isEqualTo(promotionDomain.getPromotionApplies().size());

    verify(this.promotionEntityMapper, times(1)).toEntity(promotionDomain);
    verify(this.promotionRepository, times(1)).delete(promotionEntity);
    verify(this.promotionSchedulerService, times(1)).cancelScheduledPromotion(promotionEntity);
    verify(this.kafkaTemplate, never()).send(anyString(), any());
  }

  @Test
  @DisplayName("findActivePromotionByIdReturnsOptionalOfPromotion - Should find an active promotion by id and return an Optional of Promotion")
  void findActivePromotionByIdReturnsOptionalOfPromotion() {
    final Promotion promotionDomain = this.dataMock.getPromotionsDomain().getFirst();
    final PromotionEntity promotionEntity = this.dataMock.getPromotionsEntity().getFirst();

    when(this.promotionRepository.findByIdAndIsActiveTrue(promotionDomain.getId())).thenReturn(Optional.of(promotionEntity));
    when(this.promotionEntityMapper.toDomain(promotionEntity)).thenReturn(promotionDomain);

    Optional<Promotion> foundPromotion = this.promotionGateway.findActivePromotionById(promotionDomain.getId());

    assertThat(foundPromotion.isPresent()).isTrue();
    assertThat(foundPromotion.get().getId()).isEqualTo(promotionDomain.getId());
    assertThat(foundPromotion.get().getName()).isEqualTo(promotionDomain.getName());
    assertThat(foundPromotion.get().getDescription()).isEqualTo(promotionDomain.getDescription());
    assertThat(foundPromotion.get().isActive()).isEqualTo(promotionDomain.isActive());
    assertThat(foundPromotion.get().getScope()).isEqualTo(promotionDomain.getScope());
    assertThat(foundPromotion.get().getMinimumPrice()).isEqualTo(promotionDomain.getMinimumPrice());
    assertThat(foundPromotion.get().getDiscountType()).isEqualTo(promotionDomain.getDiscountType());
    assertThat(foundPromotion.get().getDiscountValue()).isEqualTo(promotionDomain.getDiscountValue());
    assertThat(foundPromotion.get().getEndDate()).isEqualTo(promotionDomain.getEndDate());
    assertThat(foundPromotion.get().getCreatedAt()).isEqualTo(promotionDomain.getCreatedAt());
    assertThat(foundPromotion.get().getUpdatedAt()).isEqualTo(promotionDomain.getUpdatedAt());
    assertThat(foundPromotion.get().getPromotionApplies().size()).isEqualTo(promotionDomain.getPromotionApplies().size());

    verify(this.promotionRepository, times(1)).findByIdAndIsActiveTrue(promotionDomain.getId());
    verify(this.promotionEntityMapper, times(1)).toDomain(promotionEntity);
  }

  @Test
  @DisplayName("findActivePromotionByIdReturnsOptionalEmpty - Should return an Optional empty if the promotion is not found")
  void findActivePromotionByIdReturnsOptionalEmpty() {
    final UUID promotionId = this.dataMock.getPromotionsDomain().getFirst().getId();

    when(this.promotionRepository.findByIdAndIsActiveTrue(promotionId)).thenReturn(Optional.empty());

    Optional<Promotion> foundPromotion = this.promotionGateway.findActivePromotionById(promotionId);

    assertThat(foundPromotion.isEmpty()).isTrue();
    verify(this.promotionRepository, times(1)).findByIdAndIsActiveTrue(promotionId);
    verify(this.promotionEntityMapper, never()).toDomain(any(PromotionEntity.class));
  }

  @Test
  @DisplayName("updatePromotionSuccess - Should successfully update a promotion and return it")
  void updatePromotionSuccess() {
    final Promotion promotionDomain = this.dataMock.getPromotionsDomain().getFirst();
    final PromotionEntity promotionEntity = this.dataMock.getPromotionsEntity().getFirst();

    when(this.promotionEntityMapper.toEntity(promotionDomain)).thenReturn(promotionEntity);
    doNothing().when(this.promotionSchedulerService).cancelScheduledPromotion(promotionEntity);
    when(this.promotionRepository.save(promotionEntity)).thenReturn(promotionEntity);
    doNothing().when(this.promotionSchedulerService).schedulePromotionToExpire(promotionEntity);
    when(this.promotionEntityMapper.toDomain(promotionEntity)).thenReturn(promotionDomain);

    Promotion updatedPromotion = this.promotionGateway.updatePromotion(promotionDomain);

    assertThat(updatedPromotion.getId()).isEqualTo(promotionDomain.getId());
    assertThat(updatedPromotion.getName()).isEqualTo(promotionDomain.getName());
    assertThat(updatedPromotion.getDescription()).isEqualTo(promotionDomain.getDescription());
    assertThat(updatedPromotion.isActive()).isEqualTo(promotionDomain.isActive());
    assertThat(updatedPromotion.getScope()).isEqualTo(promotionDomain.getScope());
    assertThat(updatedPromotion.getMinimumPrice()).isEqualTo(promotionDomain.getMinimumPrice());
    assertThat(updatedPromotion.getDiscountType()).isEqualTo(promotionDomain.getDiscountType());
    assertThat(updatedPromotion.getDiscountValue()).isEqualTo(promotionDomain.getDiscountValue());
    assertThat(updatedPromotion.getEndDate()).isEqualTo(promotionDomain.getEndDate());
    assertThat(updatedPromotion.getCreatedAt()).isEqualTo(promotionDomain.getCreatedAt());
    assertThat(updatedPromotion.getUpdatedAt()).isEqualTo(promotionDomain.getUpdatedAt());
    assertThat(updatedPromotion.getPromotionApplies().size()).isEqualTo(promotionDomain.getPromotionApplies().size());

    verify(this.promotionEntityMapper, times(1)).toEntity(promotionDomain);
    verify(this.promotionSchedulerService, times(1)).cancelScheduledPromotion(promotionEntity);
    verify(this.promotionRepository, times(1)).save(promotionEntity);
    verify(this.promotionSchedulerService, times(1)).schedulePromotionToExpire(promotionEntity);
    verify(this.promotionEntityMapper, times(1)).toDomain(promotionEntity);
  }

  @Test
  @DisplayName("findAllPromotionsSuccess - Should successfully return all promotions")
  void findAllPromotionsSuccess() {
    final List<PromotionEntity> promotions = this.dataMock.getPromotionsEntity();
    final List<Promotion> promotionsDomain = this.dataMock.getPromotionsDomain();

    when(this.promotionRepository.findAll()).thenReturn(promotions);
    when(this.promotionEntityMapper.toDomain(promotions.get(0))).thenReturn(promotionsDomain.get(0));
    when(this.promotionEntityMapper.toDomain(promotions.get(1))).thenReturn(promotionsDomain.get(1));

    List<Promotion> allPromotions = this.promotionGateway.findAllPromotions();

    assertThat(allPromotions.size()).isEqualTo(promotions.size());
    assertThat(allPromotions.get(0)).usingRecursiveComparison().isEqualTo(promotionsDomain.get(0));
    assertThat(allPromotions.get(1)).usingRecursiveComparison().isEqualTo(promotionsDomain.get(1));

    verify(this.promotionRepository, times(1)).findAll();
    verify(this.promotionEntityMapper, times(1)).toDomain(promotions.get(0));
    verify(this.promotionEntityMapper, times(1)).toDomain(promotions.get(1));
  }

  @Test
  @DisplayName("findAllActiveOrInactivePromotionsSuccess - Should successfully return all active or inactive promotions")
  void findAllActiveOrInactivePromotionsSuccess() {
    final List<PromotionEntity> promotions = this.dataMock.getPromotionsEntity();
    final List<Promotion> promotionsDomain = this.dataMock.getPromotionsDomain();

    when(this.promotionRepository.findAllByIsActive(true)).thenReturn(promotions);
    when(this.promotionEntityMapper.toDomain(promotions.get(0))).thenReturn(promotionsDomain.get(0));
    when(this.promotionEntityMapper.toDomain(promotions.get(1))).thenReturn(promotionsDomain.get(1));

    List<Promotion> allPromotions = this.promotionGateway.findAllActiveOrInactivePromotions(true);

    assertThat(allPromotions.size()).isEqualTo(promotions.size());
    assertThat(allPromotions.get(0)).usingRecursiveComparison().isEqualTo(promotionsDomain.get(0));
    assertThat(allPromotions.get(1)).usingRecursiveComparison().isEqualTo(promotionsDomain.get(1));

    verify(this.promotionRepository, times(1)).findAllByIsActive(true);
    verify(this.promotionEntityMapper, times(1)).toDomain(promotions.get(0));
    verify(this.promotionEntityMapper, times(1)).toDomain(promotions.get(1));
  }

  @Test
  @DisplayName("findAllPromotionsByDiscountTypeSuccess - Should successfully return all promotions selected by discount type")
  void findAllPromotionsByDiscountTypeSuccess() {
    final List<PromotionEntity> promotions = this.dataMock.getPromotionsEntity();
    final List<Promotion> promotionsDomain = this.dataMock.getPromotionsDomain();
    final String discountType = "fixed_amount";

    when(this.promotionRepository.findAllByDiscountType(discountType)).thenReturn(promotions);
    when(this.promotionEntityMapper.toDomain(promotions.get(0))).thenReturn(promotionsDomain.get(0));
    when(this.promotionEntityMapper.toDomain(promotions.get(1))).thenReturn(promotionsDomain.get(1));

    List<Promotion> allPromotions = this.promotionGateway.findAllPromotionsByDiscountType(DiscountType.FIXED_AMOUNT);

    assertThat(allPromotions.size()).isEqualTo(promotions.size());
    assertThat(allPromotions.get(0)).usingRecursiveComparison().isEqualTo(promotionsDomain.get(0));
    assertThat(allPromotions.get(1)).usingRecursiveComparison().isEqualTo(promotionsDomain.get(1));

    verify(this.promotionRepository, times(1)).findAllByDiscountType(discountType);
    verify(this.promotionEntityMapper, times(1)).toDomain(promotions.get(0));
    verify(this.promotionEntityMapper, times(1)).toDomain(promotions.get(1));
  }
}
