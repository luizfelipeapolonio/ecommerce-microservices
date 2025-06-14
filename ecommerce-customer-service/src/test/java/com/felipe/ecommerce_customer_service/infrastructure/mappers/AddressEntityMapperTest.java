package com.felipe.ecommerce_customer_service.infrastructure.mappers;

import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.AddressEntity;
import com.felipe.ecommerce_customer_service.testutils.DataMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AddressEntityMapperTest {

  @Spy
  private AddressEntityMapper addressMapper;
  private final DataMock dataMock = new DataMock();

  @Test
  void shouldConvertAddressToAddressEntity() {
    Address address = this.dataMock.getAddress();

    AddressEntity convertedEntity = this.addressMapper.toEntity(address);

    assertThat(convertedEntity.getId()).isEqualTo(address.getId());
    assertThat(convertedEntity.getStreet()).isEqualTo(address.getStreet());
    assertThat(convertedEntity.getNumber()).isEqualTo(address.getNumber());
    assertThat(convertedEntity.getComplement()).isEqualTo(address.getComplement());
    assertThat(convertedEntity.getZipcode()).isEqualTo(address.getZipcode());
    assertThat(convertedEntity.getDistrict()).isEqualTo(address.getDistrict());
    assertThat(convertedEntity.getCity()).isEqualTo(address.getCity());
    assertThat(convertedEntity.getState()).isEqualTo(address.getState());
    assertThat(convertedEntity.getCountry()).isEqualTo(address.getCountry());
  }

  @Test
  void shouldConvertAddressEntityToAddress() {
    AddressEntity addressEntity = this.dataMock.getAddressEntity();

    Address convertedAddress = this.addressMapper.toDomain(addressEntity);

    assertThat(convertedAddress.getId()).isEqualTo(addressEntity.getId());
    assertThat(convertedAddress.getStreet()).isEqualTo(addressEntity.getStreet());
    assertThat(convertedAddress.getNumber()).isEqualTo(addressEntity.getNumber());
    assertThat(convertedAddress.getComplement()).isEqualTo(addressEntity.getComplement());
    assertThat(convertedAddress.getZipcode()).isEqualTo(addressEntity.getZipcode());
    assertThat(convertedAddress.getDistrict()).isEqualTo(addressEntity.getDistrict());
    assertThat(convertedAddress.getCity()).isEqualTo(addressEntity.getCity());
    assertThat(convertedAddress.getState()).isEqualTo(addressEntity.getState());
    assertThat(convertedAddress.getCountry()).isEqualTo(addressEntity.getCountry());
  }
}
