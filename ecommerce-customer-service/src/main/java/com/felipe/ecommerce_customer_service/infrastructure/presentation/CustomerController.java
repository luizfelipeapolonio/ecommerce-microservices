package com.felipe.ecommerce_customer_service.infrastructure.presentation;

import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.core.application.usecases.CreateCustomerUseCase;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CreateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.mappers.CustomerDTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
  private final CreateCustomerUseCase createCustomerUseCase;
  private final CustomerDTOMapper dtoMapper;

  public CustomerController(CreateCustomerUseCase createCustomerUseCase, CustomerDTOMapper dtoMapper) {
    this.createCustomerUseCase = createCustomerUseCase;
    this.dtoMapper = dtoMapper;
  }

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> create(@RequestBody CreateCustomerDTO customerDTO) {
    Customer customer = this.dtoMapper.toDomain(customerDTO);
    Customer createdCustomer = this.createCustomerUseCase.execute(customer, customerDTO.password());
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("Cliente de email '" + createdCustomer.getEmail() + "' criado com sucesso!");
  }
}
