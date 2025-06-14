package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.AddressDTO;
import com.felipe.ecommerce_customer_service.testutils.DataMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AddressDTOMapperTest {

  @Spy
  private AddressDTOMapper addressMapper;
  private final DataMock dataMock = new DataMock();

  @Test
  void shouldConvertAddressToAddressDTO() {
    Address address = this.dataMock.getAddress();

    AddressDTO convertedDTO = this.addressMapper.toDto(address);

    assertThat(convertedDTO.street()).isEqualTo(address.getStreet());
    assertThat(convertedDTO.number()).isEqualTo(address.getNumber());
    assertThat(convertedDTO.complement()).isEqualTo(address.getComplement());
    assertThat(convertedDTO.zipcode()).isEqualTo(address.getZipcode());
    assertThat(convertedDTO.district()).isEqualTo(address.getDistrict());
    assertThat(convertedDTO.city()).isEqualTo(address.getCity());
    assertThat(convertedDTO.state()).isEqualTo(address.getState());
    assertThat(convertedDTO.country()).isEqualTo(address.getCountry());
  }

  @Test
  void shouldConvertAddressDTOToAddress() {
    AddressDTO addressDTO = new AddressDTO(
      "Rua das Alamedas",
      "12A",
      "casa",
      "12345-678",
      "Jardim do Ypês",
      "São Paulo",
      "São Paulo",
      "Brasil"
    );

    Address address = this.addressMapper.toDomain(addressDTO);

    assertThat(address.getStreet()).isEqualTo(addressDTO.street());
    assertThat(address.getNumber()).isEqualTo(addressDTO.number());
    assertThat(address.getComplement()).isEqualTo(addressDTO.complement());
    assertThat(address.getZipcode()).isEqualTo(addressDTO.zipcode());
    assertThat(address.getDistrict()).isEqualTo(addressDTO.district());
    assertThat(address.getCity()).isEqualTo(addressDTO.city());
    assertThat(address.getState()).isEqualTo(addressDTO.state());
    assertThat(address.getCountry()).isEqualTo(addressDTO.country());
  }
}
