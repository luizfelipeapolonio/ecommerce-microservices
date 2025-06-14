package com.felipe.ecommerce_customer_service.infrastructure.gateway;

import com.felipe.ecommerce_customer_service.core.application.dtos.UpdateCustomerDTO;
import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.infrastructure.mappers.AddressEntityMapper;
import com.felipe.ecommerce_customer_service.infrastructure.mappers.CustomerEntityMapper;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.AddressEntity;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.CustomerEntity;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.repositories.CustomerRepository;
import com.felipe.ecommerce_customer_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CustomerGatewayImplTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private CustomerEntityMapper customerMapper;

  @Mock
  private AddressEntityMapper addressMapper;

  @InjectMocks
  private CustomerGatewayImpl customerGateway;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createCustomerSuccess - Should successfully create a customer and return it")
  void createCustomerSuccess() {
    Customer customer = this.dataMock.getCustomer();
    CustomerEntity customerEntity = this.dataMock.getCustomerEntity();

    when(this.customerRepository.save(customerEntity)).thenReturn(customerEntity);
    when(this.customerMapper.toEntity(customer)).thenReturn(customerEntity);
    when(this.customerMapper.toDomain(customerEntity)).thenReturn(customer);

    Customer createdCustomer = this.customerGateway.createCustomer(customer);

    assertThat(createdCustomer.getId()).isEqualTo(customer.getId());
    assertThat(createdCustomer.getEmail()).isEqualTo(customer.getEmail());
    assertThat(createdCustomer.getUsername()).isEqualTo(customer.getUsername());
    assertThat(createdCustomer.getFirstName()).isEqualTo(customer.getFirstName());
    assertThat(createdCustomer.getLastName()).isEqualTo(customer.getLastName());
    assertThat(createdCustomer.getCreatedAt()).isEqualTo(customer.getCreatedAt());
    assertThat(createdCustomer.getUpdatedAt()).isEqualTo(customer.getUpdatedAt());
    assertThat(createdCustomer.getAddress()).isNull();

    verify(this.customerMapper, times(1)).toEntity(customer);
    verify(this.customerRepository, times(1)).save(customerEntity);
    verify(this.customerMapper, times(1)).toDomain(customerEntity);
  }

  @Test
  @DisplayName("findByEmailSuccess - Should return an Optional of Customer")
  void findByEmailSuccess() {
    Customer customer = this.dataMock.getCustomer();
    CustomerEntity customerEntity = this.dataMock.getCustomerEntity();

    when(this.customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customerEntity));
    when(this.customerMapper.toDomain(customerEntity)).thenReturn(customer);

    Optional<Customer> existingCustomer = this.customerGateway.findByEmail(customer.getEmail());

    assertThat(existingCustomer).isPresent();
    assertThat(existingCustomer.get().getId()).isEqualTo(customer.getId());
    assertThat(existingCustomer.get().getEmail()).isEqualTo(customer.getEmail());
    assertThat(existingCustomer.get().getUsername()).isEqualTo(customer.getUsername());
    assertThat(existingCustomer.get().getFirstName()).isEqualTo(customer.getFirstName());
    assertThat(existingCustomer.get().getLastName()).isEqualTo(customer.getLastName());
    assertThat(existingCustomer.get().getCreatedAt()).isEqualTo(customer.getCreatedAt());
    assertThat(existingCustomer.get().getUpdatedAt()).isEqualTo(customer.getUpdatedAt());

    verify(this.customerRepository, times(1)).findByEmail(customer.getEmail());
    verify(this.customerMapper, times(1)).toDomain(customerEntity);
  }

  @Test
  @DisplayName("findByEmailReturnsEmpty - Should return an Optional empty")
  void findByEmailReturnsEmpty() {
    Customer customer = this.dataMock.getCustomer();

    when(this.customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());

    Optional<Customer> existingCustomer = this.customerGateway.findByEmail(customer.getEmail());

    assertThat(existingCustomer).isEmpty();
    verify(this.customerMapper, never()).toDomain(any(CustomerEntity.class));
  }

  @Test
  @DisplayName("updateCustomerSuccess - Should successfully update a given customer")
  void updateCustomerSuccess() {
    Customer customer = this.dataMock.getCustomer();
    CustomerEntity customerEntity = this.dataMock.getCustomerEntity();
    UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(
      "Updated username", "Updated First", "Updated Last"
    );

    CustomerEntity updatedEntity = CustomerEntity.mutate(customerEntity)
      .username(customerDTO.username())
      .firstName(customerDTO.firstName())
      .lastName(customerDTO.lastName())
      .build();
    Customer updatedCustomerDomain = Customer.mutate(customer)
      .username(updatedEntity.getUsername())
      .firstName(updatedEntity.getFirstName())
      .lastName(updatedEntity.getLastName())
      .build();

    ArgumentCaptor<CustomerEntity> entityCaptor = ArgumentCaptor.forClass(CustomerEntity.class);

    when(this.customerMapper.toEntity(customer)).thenReturn(customerEntity);
    when(this.customerRepository.save(entityCaptor.capture())).thenReturn(updatedEntity);
    when(this.customerMapper.toDomain(updatedEntity)).thenReturn(updatedCustomerDomain);

    Customer updatedCustomer = this.customerGateway.updateCustomer(customer, customerDTO);
    CustomerEntity capturedEntity = entityCaptor.getValue();

    // Captor assertion
    assertThat(capturedEntity.getId()).isEqualTo(customerEntity.getId());
    assertThat(capturedEntity.getEmail()).isEqualTo(customerEntity.getEmail());
    assertThat(capturedEntity.getUsername()).isEqualTo(customerDTO.username());
    assertThat(capturedEntity.getFirstName()).isEqualTo(customerDTO.firstName());
    assertThat(capturedEntity.getLastName()).isEqualTo(customerDTO.lastName());
    assertThat(capturedEntity.getCreatedAt()).isEqualTo(customerEntity.getCreatedAt());
    assertThat(capturedEntity.getUpdatedAt()).isEqualTo(customerEntity.getUpdatedAt());
    // Updated customer assertion
    assertThat(updatedCustomer.getId()).isEqualTo(updatedCustomerDomain.getId());
    assertThat(updatedCustomer.getEmail()).isEqualTo(updatedCustomerDomain.getEmail());
    assertThat(updatedCustomer.getUsername()).isEqualTo(updatedCustomerDomain.getUsername());
    assertThat(updatedCustomer.getFirstName()).isEqualTo(updatedCustomerDomain.getFirstName());
    assertThat(updatedCustomer.getLastName()).isEqualTo(updatedCustomerDomain.getLastName());
    assertThat(updatedCustomer.getCreatedAt()).isEqualTo(updatedCustomerDomain.getCreatedAt());
    assertThat(updatedCustomer.getUpdatedAt()).isEqualTo(updatedCustomerDomain.getUpdatedAt());

    verify(this.customerMapper, times(1)).toEntity(customer);
    verify(this.customerRepository, times(1)).save(any(CustomerEntity.class));
    verify(this.customerMapper, times(1)).toDomain(updatedEntity);
  }

  @Test
  @DisplayName("insertAddressSuccess - Should successfully insert an address to customer and return it")
  void insertAddressSuccess() {
    Customer customer = this.dataMock.getCustomer();
    Address address = this.dataMock.getAddress();
    CustomerEntity customerEntity = this.dataMock.getCustomerEntity();
    AddressEntity addressEntity = this.dataMock.getAddressEntity();

    CustomerEntity customerEntityWithAddress = CustomerEntity.mutate(customerEntity)
      .address(addressEntity)
      .build();
    Customer updatedCustomerDomain = Customer.mutate(customer)
      .address(address)
      .build();

    ArgumentCaptor<CustomerEntity> entityCaptor = ArgumentCaptor.forClass(CustomerEntity.class);

    when(this.customerMapper.toEntity(customer)).thenReturn(customerEntity);
    when(this.addressMapper.toEntity(address)).thenReturn(addressEntity);
    when(this.customerRepository.save(entityCaptor.capture())).thenReturn(customerEntityWithAddress);
    when(this.customerMapper.toDomain(customerEntityWithAddress)).thenReturn(updatedCustomerDomain);

    Customer updatedCustomer = this.customerGateway.insertAddress(customer, address);
    CustomerEntity capturedEntity = entityCaptor.getValue();

    // Captor assertion
    assertThat(capturedEntity.getAddress().getId()).isEqualTo(addressEntity.getId());
    assertThat(capturedEntity.getAddress().getStreet()).isEqualTo(addressEntity.getStreet());
    assertThat(capturedEntity.getAddress().getNumber()).isEqualTo(addressEntity.getNumber());
    assertThat(capturedEntity.getAddress().getComplement()).isEqualTo(addressEntity.getComplement());
    assertThat(capturedEntity.getAddress().getDistrict()).isEqualTo(addressEntity.getDistrict());
    assertThat(capturedEntity.getAddress().getZipcode()).isEqualTo(addressEntity.getZipcode());
    assertThat(capturedEntity.getAddress().getCity()).isEqualTo(addressEntity.getCity());
    assertThat(capturedEntity.getAddress().getState()).isEqualTo(addressEntity.getState());
    assertThat(capturedEntity.getAddress().getCountry()).isEqualTo(addressEntity.getCountry());
    // Updated customer assertion
    assertThat(updatedCustomer.getId()).isEqualTo(updatedCustomerDomain.getId());
    assertThat(updatedCustomer.getEmail()).isEqualTo(updatedCustomerDomain.getEmail());
    assertThat(updatedCustomer.getUsername()).isEqualTo(updatedCustomerDomain.getUsername());
    assertThat(updatedCustomer.getFirstName()).isEqualTo(updatedCustomerDomain.getFirstName());
    assertThat(updatedCustomer.getLastName()).isEqualTo(updatedCustomerDomain.getLastName());
    assertThat(updatedCustomer.getCreatedAt()).isEqualTo(updatedCustomerDomain.getCreatedAt());
    assertThat(updatedCustomer.getUpdatedAt()).isEqualTo(updatedCustomerDomain.getUpdatedAt());
    assertThat(updatedCustomer.getAddress()).isEqualTo(updatedCustomerDomain.getAddress());

    verify(this.customerMapper, times(1)).toEntity(customer);
    verify(this.addressMapper, times(1)).toEntity(address);
    verify(this.customerRepository, times(1)).save(any(CustomerEntity.class));
    verify(this.customerMapper, times(1)).toDomain(customerEntityWithAddress);
  }

  @Test
  @DisplayName("updatedAddressSuccess - Should successfully update an address, insert to customer and return it")
  void updateAddressSuccess() {
    Customer customer = Customer.mutate(this.dataMock.getCustomer())
      .address(this.dataMock.getAddress())
      .build();
    CustomerEntity customerEntity = CustomerEntity.mutate(this.dataMock.getCustomerEntity())
      .address(this.dataMock.getAddressEntity())
      .build();

    Address updatedAddress = Address.mutate(customer.getAddress())
      .street("Updated Street")
      .complement("Updated Complement")
      .city("Updated City")
      .build();
    AddressEntity updatedAddressEntity = AddressEntity.mutate(customerEntity.getAddress())
      .street("Updated Street")
      .complement("Updated Complement")
      .city("Updated City")
      .build();

    CustomerEntity updatedCustomerEntity = CustomerEntity.mutate(customerEntity)
      .address(updatedAddressEntity)
      .build();
    Customer updatedCustomerDomain = Customer.mutate(customer)
      .address(updatedAddress)
      .build();

    ArgumentCaptor<CustomerEntity> entityCaptor = ArgumentCaptor.forClass(CustomerEntity.class);

    when(this.customerMapper.toEntity(customer)).thenReturn(customerEntity);
    when(this.customerRepository.save(entityCaptor.capture())).thenReturn(updatedCustomerEntity);
    when(this.customerMapper.toDomain(updatedCustomerEntity)).thenReturn(updatedCustomerDomain);

    Customer updatedCustomer = this.customerGateway.updateAddress(customer, updatedAddress);
    CustomerEntity capturedEntity = entityCaptor.getValue();

    // Captor assertion
    assertThat(capturedEntity.getAddress().getStreet()).isEqualTo(updatedAddressEntity.getStreet());
    assertThat(capturedEntity.getAddress().getComplement()).isEqualTo(updatedAddressEntity.getComplement());
    assertThat(capturedEntity.getAddress().getCity()).isEqualTo(updatedAddressEntity.getCity());
    // Updated customer assertion
    assertThat(updatedCustomer.getId()).isEqualTo(customer.getId());
    assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail());
    assertThat(updatedCustomer.getUsername()).isEqualTo(customer.getUsername());
    assertThat(updatedCustomer.getFirstName()).isEqualTo(customer.getFirstName());
    assertThat(updatedCustomer.getLastName()).isEqualTo(customer.getLastName());
    assertThat(updatedCustomer.getCreatedAt()).isEqualTo(customer.getUpdatedAt());
    assertThat(updatedCustomer.getAddress()).isEqualTo(updatedAddress);

    verify(this.customerMapper, times(1)).toEntity(customer);
    verify(this.customerRepository, times(1)).save(any(CustomerEntity.class));
    verify(this.customerMapper, times(1)).toDomain(updatedCustomerEntity);
  }
}
