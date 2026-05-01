package com.felipe.ecommerce_shipping_service.infrastructure.external;

import com.felipe.ecommerce_shipping_service.core.application.dtos.CreateShipmentDTO;
import com.felipe.ecommerce_shipping_service.core.application.usecases.CreateShipmentUseCase;
import com.felipe.ecommerce_shipping_service.core.domain.Shipment;
import com.felipe.ecommerce_shipping_service.infrastructure.utils.ShippingCalculator;
import com.felipe.kafka.saga.commands.ShippingTransactionCommitCommand;
import com.felipe.kafka.saga.commands.ShippingTransactionCreateCommand;
import com.felipe.kafka.saga.replies.ShippingTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@KafkaListener(id = "shipping-transactions", topics = "order.order_transaction.shipping.commands", groupId = "shipping-service")
public class KafkaService {
  private final ShippingCalculator shippingCalculator;
  private final CreateShipmentUseCase createShipmentUseCase;
  private final KafkaTemplate<String, ShippingTransactionReply> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
  private static final String ORDER_TRANSACTION_REPLY_TOPIC = "order.order_transaction.replies";

  public KafkaService(ShippingCalculator shippingCalculator, CreateShipmentUseCase createShipmentUseCase,
                      KafkaTemplate<String, ShippingTransactionReply> kafkaTemplate) {
    this.shippingCalculator = shippingCalculator;
    this.createShipmentUseCase = createShipmentUseCase;
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaHandler
  void calculateShippingFee(ShippingTransactionCreateCommand transactionCommand) {
    logger.info("\nCommand received in Calculate Shipping Fee -> orderId: {}", transactionCommand.getOrderId());
    ShippingTransactionReply.Builder replyBuilder = ShippingTransactionReply.builder()
      .withSagaId(transactionCommand.getSagaId())
      .withTransactionId(transactionCommand.getTransactionId())
      .withOrderId(transactionCommand.getOrderId())
      .withCommand(transactionCommand.getCommand())
      .withParticipant(ShippingTransactionReply.SagaParticipant.SHIPPING);

    CompletableFuture<SendResult<String, ShippingTransactionReply>> sentMessage;
    try {
      String shippingFee = this.shippingCalculator.calculateShippingFee();

      replyBuilder.withShippingFee(shippingFee)
        .withFailureCode(ShippingTransactionReply.FailureCode.NO_APPLY)
        .withFailureMessage(null)
        .success();

      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLY_TOPIC, replyBuilder.build());
    } catch (Exception ex) {
      logger.error("Error in calculating shipping fee -> {}", ex.getMessage(), ex);
      replyBuilder.withFailureCode(ShippingTransactionReply.FailureCode.INFRASTRUCTURE_EXCEPTION)
        .withFailureMessage(ex.getMessage())
        .failure();

      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLY_TOPIC, replyBuilder.build());
    }
    sentMessage.whenComplete((result, exception) ->
      logger.info(
        "Calculating shipping fee reply posted on topic \"{}\" successfully -> status: {}",
        result.getRecordMetadata().topic(), result.getProducerRecord().value().getStatus().name()
      ));
  }

  @KafkaHandler
  void commitShipment(ShippingTransactionCommitCommand transactionCommand) {
    logger.info("\nCommand received in Commit Shipment -> orderId: {}", transactionCommand.getOrderId());
    try {
      CreateShipmentDTO shipmentDTO = new CreateShipmentDTO(
        transactionCommand.getOrderId(),
        mapToShipmentAddressDTO(transactionCommand.getShipmentAddress())
      );
      Shipment shipment = this.createShipmentUseCase.execute(shipmentDTO);
      logger.info("Shipment created successfully -> orderId: {} - address: {}", shipment.getOrderId(), shipment.getShipmentAddress());
    } catch (Exception ex) {
      logger.error("Error in commiting shipment -> {}", ex.getMessage(), ex);
    }
  }

  private CreateShipmentDTO.ShipmentAddress mapToShipmentAddressDTO(ShippingTransactionCommitCommand.ShipmentAddress shipmentAddress) {
    return new CreateShipmentDTO.ShipmentAddress(
      shipmentAddress.street(),
      shipmentAddress.number(),
      shipmentAddress.complement(),
      shipmentAddress.district(),
      shipmentAddress.zipcode(),
      shipmentAddress.city(),
      shipmentAddress.state(),
      shipmentAddress.country()
    );
  }
}
