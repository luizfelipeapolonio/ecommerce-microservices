package com.felipe.ecommerce_customer_service.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "addresses")
public class AddressEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String street;

  @Column(nullable = false, length = 20)
  private String number;

  @Column(nullable = false, length = 100)
  private String complement;

  @Column(nullable = false)
  private String district;

  @Column(nullable = false, length = 30)
  private String zipcode;

  @Column(nullable = false, length = 100)
  private String city;

  @Column(nullable = false, length = 100)
  private String state;

  @Column(nullable = false, length = 100)
  private String country;

  @OneToOne(mappedBy = "address")
  private CustomerEntity customer;

  protected AddressEntity () {}

  public Long getId() {
    return this.id;
  }

  public String getStreet() {
    return this.street;
  }

  public String getNumber() {
    return this.number;
  }

  public String getComplement() {
    return this.complement;
  }

  public String getDistrict() {
    return this.district;
  }

  public String getZipcode() {
    return this.zipcode;
  }

  public String getCity() {
    return this.city;
  }

  public String getState() {
    return this.state;
  }

  public String getCountry() {
    return this.country;
  }

  public CustomerEntity getCustomer() {
    return this.customer;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(AddressEntity entity) {
    return new Builder(entity);
  }

  protected AddressEntity(Builder builder) {
    this.id = builder.id;
    this.street = builder.street;
    this.number = builder.number;
    this.complement = builder.complement;
    this.district = builder.district;
    this.zipcode = builder.zipcode;
    this.city = builder.city;
    this.state = builder.state;
    this.country = builder.country;
    this.customer = builder.customer;
  }

  public static class Builder {
    private Long id;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String zipcode;
    private String city;
    private String state;
    private String country;
    private CustomerEntity customer;

    private Builder() {}

    private Builder(AddressEntity entity) {
      this.id = entity.getId();
      this.street = entity.getStreet();
      this.number= entity.getNumber();
      this.complement = entity.getComplement();
      this.district = entity.getDistrict();
      this.zipcode = entity.getZipcode();
      this.city = entity.getCity();
      this.state = entity.getState();
      this.country = entity.getCountry();
      this.customer = entity.getCustomer();
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder street(String street) {
      this.street = street;
      return this;
    }

    public Builder number(String number) {
      this.number = number;
      return this;
    }

    public Builder complement(String complement) {
      this.complement = complement;
      return this;
    }

    public Builder district(String district) {
      this.district = district;
      return this;
    }

    public Builder zipcode(String zipcode) {
      this.zipcode = zipcode;
      return this;
    }

    public Builder city(String city) {
      this.city = city;
      return this;
    }

    public Builder state(String state) {
      this.state = state;
      return this;
    }

    public Builder country(String country) {
      this.country = country;
      return this;
    }

    public Builder customer(CustomerEntity customer) {
      this.customer = customer;
      return this;
    }

    public AddressEntity build() {
      return new AddressEntity(this);
    }
  }
}
