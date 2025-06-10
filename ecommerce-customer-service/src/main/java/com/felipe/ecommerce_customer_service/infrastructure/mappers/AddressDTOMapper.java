package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.AddressDTO;
import org.springframework.stereotype.Component;

@Component
public class AddressDTOMapper {

  public AddressDTO toDto(Address address) {
    return new AddressDTO(
      address.getStreet(),
      address.getNumber(),
      address.getComplement(),
      address.getDistrict(),
      address.getZipcode(),
      address.getCity(),
      address.getState(),
      address.getCountry()
    );
  }

  public Address toDomain(AddressDTO addressDTO) {
    return Address.builder()
      .street(addressDTO.street())
      .number(addressDTO.number())
      .complement(addressDTO.complement())
      .district(addressDTO.district())
      .zipcode(addressDTO.zipcode())
      .city(addressDTO.city())
      .state(addressDTO.state())
      .country(addressDTO.country())
      .build();
  }
}
