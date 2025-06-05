package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressEntityMapper {

  public AddressEntity toEntity(Address address) {
    return AddressEntity.builder()
      .id(address.getId())
      .street(address.getStreet())
      .number(address.getNumber())
      .complement(address.getComplement())
      .district(address.getDistrict())
      .zipcode(address.getZipcode())
      .city(address.getCity())
      .state(address.getState())
      .country(address.getCountry())
      .build();
  }

  public Address toDomain(AddressEntity entity) {
    return Address.builder()
      .id(entity.getId())
      .street(entity.getStreet())
      .number(entity.getNumber())
      .complement(entity.getComplement())
      .district(entity.getDistrict())
      .zipcode(entity.getZipcode())
      .city(entity.getCity())
      .state(entity.getState())
      .country(entity.getCountry())
      .build();
  }
}
