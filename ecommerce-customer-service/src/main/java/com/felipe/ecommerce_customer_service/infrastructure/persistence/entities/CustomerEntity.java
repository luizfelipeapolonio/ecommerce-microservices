package com.felipe.ecommerce_customer_service.infrastructure.persistence.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class CustomerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, length = 100)
  private String username;

  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  protected CustomerEntity() {}

  public UUID getId() {
    return this.id;
  }

  public String getEmail() {
    return this.email;
  }

  public String getUsername() {
    return this.username;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public AddressEntity getAddress() {
    return this.address;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(CustomerEntity entity) {
    return new Builder(entity);
  }

  protected CustomerEntity(Builder builder) {
    this.id = builder.id;
    this.email = builder.email;
    this.username = builder.username;
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
    this.address = builder.address;
  }

  public static class Builder {
    private UUID id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AddressEntity address;

    private Builder() {}

    private Builder(CustomerEntity entity) {
      this.id = entity.getId();
      this.email = entity.getEmail();
      this.username = entity.getUsername();
      this.firstName = entity.getFirstName();
      this.lastName = entity.getLastName();
      this.createdAt = entity.getCreatedAt();
      this.updatedAt = entity.getUpdatedAt();
      this.address = entity.getAddress();
    }

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Builder address(AddressEntity address) {
      this.address = address;
      return this;
    }

    public CustomerEntity build() {
      return new CustomerEntity(this);
    }
  }
}
