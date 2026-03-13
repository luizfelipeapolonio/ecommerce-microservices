package com.felipe.kafka.saga.replies;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;
import java.util.UUID;

@JsonDeserialize(builder = InventoryTransactionReply.Builder.class)
public final class InventoryTransactionReply extends ReplyTransaction {
  private final List<ProductData> products;

  private InventoryTransactionReply(Builder builder) {
    super(
      builder.sagaId,
      builder.transactionId,
      builder.orderId,
      builder.command,
      builder.status,
      builder.failureCode,
      builder.failureMessage,
      builder.participant
    );
    this.products = builder.products;
  }

  public List<ProductData> getProducts() {
    return this.products;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class ProductData {
    private UUID id;
    private String name;
    private String unitPrice;
    private long quantity;
    private boolean withDiscount;
    private String discountType;
    private String discountValue;

    public ProductData() {
    }

    public ProductData(UUID id) {
      this.id = id;
    }

    public UUID getId() {
      return this.id;
    }

    public ProductData setId(UUID id) {
      this.id = id;
      return this;
    }

    public String getName() {
      return this.name;
    }

    public ProductData setName(String name) {
      this.name = name;
      return this;
    }

    public String getUnitPrice() {
      return this.unitPrice;
    }

    public ProductData setUnitPrice(String unitPrice) {
      this.unitPrice = unitPrice;
      return this;
    }

    public long getQuantity() {
      return this.quantity;
    }

    public ProductData setQuantity(long quantity) {
      this.quantity = quantity;
      return this;
    }

    public boolean isItWithDiscount() {
      return this.withDiscount;
    }

    public ProductData setWithDiscount(boolean withDiscount) {
      this.withDiscount = withDiscount;
      return this;
    }

    public String getDiscountType() {
      return this.discountType;
    }

    public ProductData setDiscountType(String discountType) {
      this.discountType = discountType;
      return this;
    }

    public String getDiscountValue() {
      return this.discountValue;
    }

    public ProductData setDiscountValue(String discountValue) {
      this.discountValue = discountValue;
      return this;
    }
  }

  @JsonPOJOBuilder
  public static class Builder {
    private UUID sagaId;
    private UUID transactionId;
    private UUID orderId;
    private List<ProductData> products;
    private Command command;
    private Status status;
    private FailureCode failureCode;
    private String failureMessage;
    private SagaParticipant participant;

    private Builder() {
    }

    public Builder withSagaId(UUID sagaId) {
      this.sagaId = sagaId;
      return this;
    }

    public Builder withTransactionId(UUID transactionId) {
      this.transactionId = transactionId;
      return this;
    }

    public Builder withOrderId(UUID orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder withProducts(List<ProductData> products) {
      this.products = products;
      return this;
    }

    public Builder withCommand(Command command) {
      this.command = command;
      return this;
    }

    // For jackson deserialization purposes only
    // Use success or fail methods to assign a status
    // in a more direct and readable way
    Builder withStatus(Status status) {
      this.status = status;
      return this;
    }

    public Builder withFailureCode(FailureCode failureCode) {
      this.failureCode = failureCode;
      return this;
    }

    public Builder withFailureMessage(String failureMessage) {
      this.failureMessage = failureMessage;
      return this;
    }

    public Builder withParticipant(SagaParticipant participant) {
      this.participant = participant;
      return this;
    }

    public Builder success() {
      this.status = Status.SUCCESS;
      return this;
    }

    public Builder failure() {
      this.status = Status.FAILURE;
      return this;
    }

    public InventoryTransactionReply build() {
      return new InventoryTransactionReply(this);
    }
  }
}
