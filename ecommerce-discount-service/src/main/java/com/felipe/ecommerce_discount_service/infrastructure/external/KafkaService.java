package com.felipe.ecommerce_discount_service.infrastructure.external;

import com.felipe.ecommerce_discount_service.core.application.exceptions.CouponAlreadyAppliedException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidCouponException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.MinimumPriceException;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.ApplyCouponUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.kafka.saga.commands.DiscountTransactionCreateCommand;
import com.felipe.kafka.saga.replies.DiscountTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaService {
  private final ApplyCouponUseCase applyCouponUseCase;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
  private static final String ORDER_TRANSACTION_REPLIES_TOPIC = "order.order_transaction.replies";

  public KafkaService(ApplyCouponUseCase applyCouponUseCase, KafkaTemplate<String, Object> kafkaTemplate) {
    this.applyCouponUseCase = applyCouponUseCase;
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(topics = "order.order_transaction.discount.commands", groupId = "discount-service")
  void applyCoupon(DiscountTransactionCreateCommand transactionCommand) {
    logger.info(
      "\nCommand received in applyCoupon:\ncommand: {}\ncouponCode: {}\norderAmount: {}\ncustomerId: {}",
      transactionCommand.getCommand(), transactionCommand.getCouponCode(), transactionCommand.getOrderAmount(), transactionCommand.getCustomerId()
    );
    DiscountTransactionReply.Builder replyBuilder = DiscountTransactionReply.builder()
      .withSagaId(transactionCommand.getSagaId())
      .withTransactionId(transactionCommand.getTransactionId())
      .withCommand(transactionCommand.getCommand())
      .withOrderId(transactionCommand.getOrderId())
      .withParticipant(DiscountTransactionReply.SagaParticipant.DISCOUNT);

    CompletableFuture<SendResult<String, Object>> sentMessage;
    try {
      Coupon coupon = this.applyCouponUseCase.execute(
        transactionCommand.getCouponCode(),
        transactionCommand.getOrderAmount(),
        transactionCommand.getOrderId(),
        transactionCommand.getCustomerId()
      );
      replyBuilder.withCouponCode(coupon.getCouponCode())
        .withDiscountType(coupon.getDiscountType())
        .withDiscountValue(coupon.getDiscountValue())
        .withFailureCode(DiscountTransactionReply.FailureCode.NO_APPLY)
        .withFailureMessage(null)
        .success();
      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLIES_TOPIC, replyBuilder.build());
    } catch (Exception ex) {
      mapBusinessException(ex, replyBuilder);
      logger.error("Error in applyCoupon -> message: {}", ex.getMessage(), ex);
      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLIES_TOPIC, replyBuilder.build());
    }
    sentMessage.whenComplete((result, exception) -> {
      DiscountTransactionReply reply = (DiscountTransactionReply) result.getProducerRecord().value();
      logger.info(
        "Apply coupon posted on topic \"{}\" successfully -> status: {}",
        result.getRecordMetadata().topic(), reply.getStatus().name()
      );
    });
  }

  private void mapBusinessException(Exception exception, DiscountTransactionReply.Builder replyBuilder) {
    switch (exception) {
      case DataNotFoundException ex -> replyBuilder
        .withFailureCode(DiscountTransactionReply.FailureCode.BUSINESS_EXCEPTION)
        .withFailureMessage(ex.getMessage())
        .failure();
      case InvalidCouponException ex -> replyBuilder
        .withFailureCode(DiscountTransactionReply.FailureCode.BUSINESS_EXCEPTION)
        .withFailureMessage(ex.getMessage())
        .failure();
      case MinimumPriceException ex -> replyBuilder
        .withFailureCode(DiscountTransactionReply.FailureCode.BUSINESS_EXCEPTION)
        .withFailureMessage(ex.getMessage())
        .failure();
      case CouponAlreadyAppliedException ex -> replyBuilder
        .withFailureCode(DiscountTransactionReply.FailureCode.BUSINESS_EXCEPTION)
        .withFailureMessage(ex.getMessage())
        .failure();
      default -> replyBuilder.withFailureCode(DiscountTransactionReply.FailureCode.INFRASTRUCTURE_EXCEPTION)
        .withFailureMessage(exception.getMessage())
        .failure();
    }
  }
}
