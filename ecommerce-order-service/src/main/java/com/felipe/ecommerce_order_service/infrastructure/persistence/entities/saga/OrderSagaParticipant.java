package com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga;

import com.felipe.kafka.saga.enums.SagaParticipant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_saga_participants")
public class OrderSagaParticipant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SagaParticipant name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SagaParticipantStatus status = SagaParticipantStatus.PENDING;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "saga_id", nullable = false)
  private OrderSaga saga;

  public OrderSagaParticipant() {
  }

  public OrderSagaParticipant(SagaParticipant name) {
    this.name = name;
  }

  public  Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SagaParticipant getName() {
    return this.name;
  }

  public void setName(SagaParticipant name) {
    this.name = name;
  }

  public SagaParticipantStatus getStatus() {
    return this.status;
  }

  public void setStatus(SagaParticipantStatus status) {
    this.status = status;
  }

  public OrderSaga getSaga() {
    return this.saga;
  }

  public void setSaga(OrderSaga saga) {
    this.saga = saga;
  }
}
