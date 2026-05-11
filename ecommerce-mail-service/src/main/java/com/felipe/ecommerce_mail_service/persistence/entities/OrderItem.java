package com.felipe.ecommerce_mail_service.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private long quantity;

  @Column(nullable = false, length = 300)
  private String name;

  @Column(nullable = false, length = 100)
  private String price;

  @ManyToOne
  @JoinColumn(name = "mail_to_id")
  private MailTo mailTo;

  public OrderItem() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public long getQuantity() {
    return this.quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPrice() {
    return this.price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public MailTo getMailTo() {
    return this.mailTo;
  }

  public void setMailTo(MailTo mailTo) {
    this.mailTo = mailTo;
  }
}
