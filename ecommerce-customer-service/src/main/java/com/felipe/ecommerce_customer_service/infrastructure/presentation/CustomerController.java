package com.felipe.ecommerce_customer_service.infrastructure.presentation;

import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.core.application.usecases.CreateCustomerUseCase;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CreateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerResponseDTO;
import com.felipe.ecommerce_customer_service.infrastructure.mappers.CustomerDTOMapper;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponsePayload<CustomerResponseDTO> create(@RequestBody CreateCustomerDTO customerDTO) {
    Customer customer = this.dtoMapper.toDomain(customerDTO);
    Customer createdCustomer = this.createCustomerUseCase.execute(customer, customerDTO.password());

    return new ResponsePayload.Builder<CustomerResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Conta criada com sucesso")
      .payload(this.dtoMapper.toResponseDTO(createdCustomer))
      .build();
  }

  @GetMapping("/test")
  public String test() {
    return "You accessed a protected resource";
  }
}
