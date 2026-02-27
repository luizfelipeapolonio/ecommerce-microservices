package com.felipe.ecommerce_inventory_service.infrastructure.external;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ReservationAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.UnavailableProductException;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.DeleteReservationUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.ReserveProductUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.ProductRepository;
import com.felipe.kafka.ExpiredPromotionKafkaDTO;
import com.felipe.kafka.saga.commands.InventoryTransactionCancelCommand;
import com.felipe.kafka.saga.commands.InventoryTransactionCreateCommand;
import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import com.felipe.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@KafkaListener(id = "inventory-transactions", topics = {
  "expired-promotion",
  "order.order_transaction.inventory.commands"
}, groupId = "inventory-service")
public class KafkaService {
  private final ProductRepository productRepository;
  private final ReserveProductUseCase reserveProductUseCase;
  private final DeleteReservationUseCase deleteReservationUseCase;
  private final KafkaTemplate<String, InventoryTransactionReply> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
  private static final String ORDER_TRANSACTION_REPLIES_TOPIC = "order.order_transaction.replies";

  public KafkaService(ProductRepository productRepository,
                      ReserveProductUseCase reserveProductUseCase,
                      DeleteReservationUseCase deleteReservationUseCase,
                      KafkaTemplate<String, InventoryTransactionReply> kafkaTemplate) {
    this.productRepository = productRepository;
    this.reserveProductUseCase = reserveProductUseCase;
    this.kafkaTemplate = kafkaTemplate;
    this.deleteReservationUseCase = deleteReservationUseCase;
  }

  @KafkaHandler
  void removeExpiredPromotionFromProducts(ExpiredPromotionKafkaDTO expiredPromotionDTO) {
    logger.info("=== Removing expired promotions ===");
    this.productRepository.findAllByPromotionId(expiredPromotionDTO.promotionId())
      .forEach(product -> {
        logger.info("Removing promotion \"{}\" from product \"{}\"", expiredPromotionDTO.promotionId(), product.getName());
        final ProductEntity updatedEntity = ProductEntity.mutate(product)
          .withDiscount(false)
          .promotionId(null)
          .discountType(null)
          .discountValue(null)
          .build();

        this.productRepository.save(updatedEntity);
      });
  }

  @KafkaHandler
  void reserveProduct(InventoryTransactionCreateCommand transactionCommand) {
    logger.info(
      "Command received in Reserve Product -> productId: {} - orderId: {} - command: {}",
      transactionCommand.getProductId(), transactionCommand.getOrderId(), transactionCommand.getCommand().name()
    );
    InventoryTransactionReply.ProductData productData = new InventoryTransactionReply.ProductData(transactionCommand.getProductId());
    InventoryTransactionReply.Builder replyBuilder = InventoryTransactionReply.builder()
      .withSagaId(transactionCommand.getSagaId())
      .withTransactionId(transactionCommand.getTransactionId())
      .withOrderId(transactionCommand.getOrderId())
      .withProduct(productData)
      .withCommand(transactionCommand.getCommand())
      .withParticipant(InventoryTransactionReply.SagaParticipant.INVENTORY);

    CompletableFuture<SendResult<String, InventoryTransactionReply>> sentMessage;
    try {
      Pair<Product, Reservation> reservation = this.reserveProductUseCase.execute(
        transactionCommand.getProductId(), transactionCommand.getOrderId(), transactionCommand.getProductQuantity()
      );

      productData.setName(reservation.t1().getName())
        .setUnitPrice(reservation.t1().getUnitPrice().toString())
        .setWithDiscount(reservation.t1().isItWithDiscount())
        .setDiscountType(reservation.t1().getDiscountType())
        .setDiscountValue(reservation.t1().getDiscountValue());

      replyBuilder.withFailureCode(InventoryTransactionReply.FailureCode.NO_APPLY)
        .withFailureMessage(null)
        .success();

      logger.info("Reservation done -> {}", reservation.t2());
      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLIES_TOPIC, replyBuilder.build());
    } catch (Exception ex) {
      mapBusinessExceptions(ex, replyBuilder);
      logger.error("Error in product reservation: {} - exception: {}", ex.getMessage(), ex.getClass().getName());
      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLIES_TOPIC, replyBuilder.fail().build());
    }
    sentMessage.whenComplete((result, exception) ->
      logger.info("Reserve product posted on topic \"{}\" successfully -> status: {}",
        result.getRecordMetadata().topic(), result.getProducerRecord().value().getStatus().name()
      ));
  }

  @KafkaHandler
  void cancelReservation(InventoryTransactionCancelCommand transactionCommand) {
    logger.info(
      "Command received in Cancel Reservation -> productId: {} - orderId: {} - command: {}",
      transactionCommand.getProductId(), transactionCommand.getOrderId(), transactionCommand.getCommand().name()
    );
    InventoryTransactionReply.Builder replyBuilder = InventoryTransactionReply.builder()
      .withSagaId(transactionCommand.getSagaId())
      .withTransactionId(transactionCommand.getTransactionId())
      .withOrderId(transactionCommand.getOrderId())
      .withProduct(new InventoryTransactionReply.ProductData(transactionCommand.getProductId()))
      .withCommand(transactionCommand.getCommand())
      .withParticipant(InventoryTransactionReply.SagaParticipant.INVENTORY);
    CompletableFuture<SendResult<String, InventoryTransactionReply>> sentMessage;

    if (transactionCommand.getFailureCode() == InventoryTransactionReply.FailureCode.BUSINESS_EXCEPTION) {
      replyBuilder.withFailureCode(InventoryTransactionReply.FailureCode.NO_APPLY)
        .withFailureMessage(null)
        .success();
      logger.info("Reservation cancelled successfully -> failureCode: {}", transactionCommand.getFailureCode());
      this.kafkaTemplate.send(ORDER_TRANSACTION_REPLIES_TOPIC, replyBuilder.build());
      return;
    }
    try {
      this.deleteReservationUseCase.execute(transactionCommand.getProductId(), transactionCommand.getOrderId());
      replyBuilder.withFailureCode(InventoryTransactionReply.FailureCode.NO_APPLY)
        .withFailureMessage(null)
        .success();
      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLIES_TOPIC, replyBuilder.build());
    } catch (Exception ex) {
      mapBusinessExceptions(ex, replyBuilder);
      logger.error("Error in cancel reservation: {} - exception: {}", ex.getMessage(), ex.getClass().getName());
      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLIES_TOPIC, replyBuilder.fail().build());
    }
    sentMessage.whenComplete((result, exception) -> {
      logger.info(
        "Cancel reservation posted on topic \"{}\" successfully -> status: {}",
        result.getRecordMetadata().topic(), result.getProducerRecord().value().getStatus().name()
      );
    });
  }

  private void mapBusinessExceptions(Exception exception, InventoryTransactionReply.Builder replyBuilder) {
    switch (exception) {
      case DataNotFoundException ex -> replyBuilder
        .withFailureCode(InventoryTransactionReply.FailureCode.BUSINESS_EXCEPTION)
        .withFailureMessage(ex.getMessage());
      case ReservationAlreadyExistsException ex -> replyBuilder
        .withFailureCode(InventoryTransactionReply.FailureCode.BUSINESS_EXCEPTION)
        .withFailureMessage(ex.getMessage());
      case UnavailableProductException ex -> replyBuilder
        .withFailureCode(InventoryTransactionReply.FailureCode.BUSINESS_EXCEPTION)
        .withFailureMessage(ex.getMessage());
      default -> replyBuilder.withFailureCode(InventoryTransactionReply.FailureCode.INFRASTRUCTURE_EXCEPTION)
        .withFailureMessage(exception.getMessage());
    }
  }
}
