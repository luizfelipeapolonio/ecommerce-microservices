package com.felipe.ecommerce_customer_service.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {
  private final UUID id;
  private final String email;
  private final String username;
  private final String firstName;
  private final String lastName;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Address address;

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

  public Address getAddress() {
    return this.address;
  }

  public static Builder builder() {
    return new Builder();
  }

  private Customer(Builder builder) {
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
    private Address address;

    private Builder() {}

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

    public Builder address(Address address) {
      this.address = address;
      return this;
    }

    public Customer build() {
      return new Customer(this);
    }
  }
}
