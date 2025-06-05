package com.felipe.ecommerce_customer_service.core.domain;

public class Address {
  private final Long id;
  private final String street;
  private final String number;
  private final String complement;
  private final String district;
  private final String zipcode;
  private final String city;
  private final String state;
  private final String country;

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

  public static Builder builder() {
    return new Builder();
  }

  private Address(Builder builder) {
    this.id = builder.id;
    this.street = builder.street;
    this.number = builder.number;
    this.complement = builder.complement;
    this.district = builder.district;
    this.zipcode = builder.zipcode;
    this.city = builder.city;
    this.state = builder.state;
    this.country = builder.country;
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

    private Builder() {}

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

    public Address build() {
      return new Address(this);
    }
  }
}
