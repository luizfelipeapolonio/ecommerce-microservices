package com.felipe.ecommerce_customer_service.infrastructure.gateway;

import com.felipe.ecommerce_customer_service.core.application.dtos.UpdateCustomerDTO;
import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.infrastructure.external.CartService;
import com.felipe.ecommerce_customer_service.infrastructure.mappers.AddressEntityMapper;
import com.felipe.ecommerce_customer_service.infrastructure.mappers.CustomerEntityMapper;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.AddressEntity;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.CustomerEntity;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.repositories.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerGatewayImpl implements CustomerGateway {
  private final CustomerRepository customerRepository;
  private final CustomerEntityMapper customerMapper;
  private final AddressEntityMapper addressMapper;
  private final CartService cartService;

  public CustomerGatewayImpl(CustomerRepository customerRepository,
                             CustomerEntityMapper customerMapper,
                             AddressEntityMapper addressMapper,
                             CartService cartService) {
    this.customerRepository = customerRepository;
    this.customerMapper = customerMapper;
    this.addressMapper = addressMapper;
    this.cartService = cartService;
  }

  @Override
  public Customer createCustomer(Customer customer) {
    CustomerEntity customerEntity = this.customerMapper.toEntity(customer);
    CustomerEntity createdCustomerEntity = this.customerRepository.save(customerEntity);
    this.cartService.createCart(new CartService.CreateCartDTO(createdCustomerEntity.getId()));
    return this.customerMapper.toDomain(createdCustomerEntity);
  }

  @Override
  public Optional<Customer> findByEmail(String email) {
    return this.customerRepository.findByEmail(email).map(this.customerMapper::toDomain);
  }

  @Override
  public Customer updateCustomer(Customer customer, UpdateCustomerDTO customerDTO) {
    CustomerEntity customerProfile = this.customerMapper.toEntity(customer);
    CustomerEntity.Builder customerBuilder = CustomerEntity.mutate(customerProfile);

    if(customerDTO.username() != null) {
      customerBuilder.username(customerDTO.username());
    }
    if(customerDTO.firstName() != null) {
      customerBuilder.firstName(customerDTO.firstName());
    }
    if(customerDTO.lastName() != null) {
      customerBuilder.lastName(customerDTO.lastName());
    }

    CustomerEntity updatedCustomer = this.customerRepository.save(customerBuilder.build());
    return this.customerMapper.toDomain(updatedCustomer);
  }

  @Override
  public Customer insertAddress(Customer customer, Address address) {
    CustomerEntity customerEntity = this.customerMapper.toEntity(customer);
    AddressEntity addressEntity = this.addressMapper.toEntity(address);
    CustomerEntity updatedCustomerEntity = CustomerEntity.mutate(customerEntity)
      .address(addressEntity)
      .build();

    CustomerEntity updatedCustomer = this.customerRepository.save(updatedCustomerEntity);
    return this.customerMapper.toDomain(updatedCustomer);
  }

  @Override
  public Customer updateAddress(Customer customer, Address address) {
    CustomerEntity customerEntity = this.customerMapper.toEntity(customer);
    AddressEntity addressEntity = customerEntity.getAddress();
    AddressEntity.Builder addressBuilder = AddressEntity.mutate(addressEntity);

    if(address.getStreet() != null) {
      addressBuilder.street(address.getStreet());
    }
    if(address.getNumber() != null) {
      addressBuilder.number(address.getNumber());
    }
    if(address.getComplement() != null) {
      addressBuilder.complement(address.getComplement());
    }
    if(address.getDistrict() != null) {
      addressBuilder.district(address.getDistrict());
    }
    if(address.getZipcode() != null) {
      addressBuilder.zipcode(address.getZipcode());
    }
    if(address.getCity() != null) {
      addressBuilder.city(address.getCity());
    }
    if(address.getState() != null) {
      addressBuilder.state(address.getState());
    }
    if(address.getCountry() != null) {
      addressBuilder.country(address.getCountry());
    }

    CustomerEntity updatedCustomerEntity = CustomerEntity.mutate(customerEntity)
      .address(addressBuilder.build())
      .build();

    CustomerEntity updatedCustomer = this.customerRepository.save(updatedCustomerEntity);
    return this.customerMapper.toDomain(updatedCustomer);
  }
}
