package com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga;

import com.felipe.kafka.saga.replies.ReplyTransaction;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_saga")
public class OrderSaga {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "order_id", nullable = false)
  private UUID orderId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private SagaStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "failure_code", length = 30)
  private ReplyTransaction.FailureCode failureCode;

  @Column(name = "failure_reason")
  private String failureReason;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "saga", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderSagaParticipant> participants = new ArrayList<>();

  public OrderSaga() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public OrderSaga id(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public OrderSaga orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public SagaStatus getStatus() {
    return this.status;
  }

  public void setStatus(SagaStatus status) {
    this.status = status;
  }

  public OrderSaga status(SagaStatus status) {
    this.status = status;
    return this;
  }

  public ReplyTransaction.FailureCode getFailureCode() {
    return this.failureCode;
  }

  public void setFailureCode(ReplyTransaction.FailureCode failureCode) {
    this.failureCode = failureCode;
  }

  public OrderSaga failureCode(ReplyTransaction.FailureCode failureCode) {
    this.failureCode = failureCode;
    return this;
  }

  public String getFailureReason() {
    return this.failureReason;
  }

  public void setFailureReason(String failureReason) {
    this.failureReason = failureReason;
  }

  public OrderSaga failureReason(String failureReason) {
    this.failureReason = failureReason;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OrderSaga createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public OrderSaga updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public List<OrderSagaParticipant> getParticipants() {
    return this.participants;
  }

  public void setParticipants(List<OrderSagaParticipant> participants) {
    this.participants = participants;
  }

  public OrderSaga addParticipant(OrderSagaParticipant participant) {
    participant.setSaga(this);
    participants.add(participant);
    return this;
  }

  public void markParticipantSuccess(ReplyTransaction.SagaParticipant participantName) {
    mutateParticipant(participantName, SagaParticipantStatus.SUCCESS, null);
  }

  public void markParticipantFailed(ReplyTransaction.SagaParticipant participantName) {
    mutateParticipant(participantName, SagaParticipantStatus.FAILURE, null);
  }

  public void markParticipantProcessing(ReplyTransaction.SagaParticipant participantName) {
    mutateParticipant(participantName, SagaParticipantStatus.PROCESSING, null);
  }

  public void setParticipantDetails(ReplyTransaction.SagaParticipant participantName, String details) {
    mutateParticipant(participantName, null, details);
  }

  private void mutateParticipant(ReplyTransaction.SagaParticipant participantName, @Nullable SagaParticipantStatus status, @Nullable String details) {
    for (OrderSagaParticipant participant : this.participants) {
      if (participant.getName() == participantName) {
        if (status != null) {
          participant.setStatus(status);
        }
        if (details != null) {
          participant.setDetails(details);
        }
        break;
      }
    }
  }
}
