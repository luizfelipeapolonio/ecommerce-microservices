package com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "shipping_fees")
public class ShippingFeeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(length = 10)
  private Distance distance;

  @Enumerated(EnumType.STRING)
  @Column(length = 15)
  private Weight weight;

  private BigDecimal price;

  public ShippingFeeEntity() {
  }

  public ShippingFeeEntity(Distance distance, Weight weight, BigDecimal price) {
    this.distance = distance;
    this.weight = weight;
    this.price = price;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Distance getDistance() {
    return this.distance;
  }

  public void setDistance(Distance distance) {
    this.distance = distance;
  }

  public Weight getWeight() {
    return this.weight;
  }

  public void setWeight(Weight weight) {
    this.weight = weight;
  }

  public BigDecimal getPrice() {
    return this.price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
