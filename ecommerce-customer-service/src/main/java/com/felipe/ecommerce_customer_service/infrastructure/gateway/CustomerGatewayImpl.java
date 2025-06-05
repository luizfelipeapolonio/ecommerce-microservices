package com.felipe.ecommerce_customer_service.infrastructure.gateway;

import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.infrastructure.mappers.CustomerEntityMapper;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.CustomerEntity;
import com.felipe.ecommerce_customer_service.infrastructure.persistence.repositories.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerGatewayImpl implements CustomerGateway {
  private final CustomerRepository customerRepository;
  private final CustomerEntityMapper mapper;

  public CustomerGatewayImpl(CustomerRepository customerRepository, CustomerEntityMapper mapper) {
    this.customerRepository = customerRepository;
    this.mapper = mapper;
  }

  @Override
  public Customer createCustomer(Customer customer) {
    CustomerEntity customerEntity = this.mapper.toEntity(customer);
    CustomerEntity createdCustomerEntity = this.customerRepository.save(customerEntity);
    return this.mapper.toDomain(createdCustomerEntity);
  }

  @Override
  public Optional<Customer> findByEmail(String email) {
    return this.customerRepository.findByEmail(email).map(this.mapper::toDomain);
  }
}
