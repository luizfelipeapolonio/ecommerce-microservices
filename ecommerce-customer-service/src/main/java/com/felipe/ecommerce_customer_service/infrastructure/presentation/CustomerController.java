package com.felipe.ecommerce_customer_service.infrastructure.presentation;

import com.felipe.ecommerce_customer_service.core.application.usecases.GetCustomerByEmailUseCase;
import com.felipe.ecommerce_customer_service.core.application.usecases.InsertAddressUseCase;
import com.felipe.ecommerce_customer_service.core.application.usecases.UpdateAddressUseCase;
import com.felipe.ecommerce_customer_service.core.application.usecases.UpdateCustomerProfileUseCase;
import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.core.application.usecases.CreateCustomerUseCase;
import com.felipe.ecommerce_customer_service.infrastructure.config.openapi.CustomerApi;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.AddressDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CreateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_customer_service.infrastructure.dtos.CustomerDTO;
import com.felipe.ecommerce_customer_service.core.application.dtos.UpdateCustomerDTO;
import com.felipe.ecommerce_customer_service.infrastructure.mappers.AddressDTOMapper;
import com.felipe.ecommerce_customer_service.infrastructure.mappers.CustomerDTOMapper;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController implements CustomerApi {
  private final CreateCustomerUseCase createCustomerUseCase;
  private final GetCustomerByEmailUseCase getCustomerByEmailUseCase;
  private final UpdateCustomerProfileUseCase updateCustomerProfileUseCase;
  private final InsertAddressUseCase insertAddressUseCase;
  private final UpdateAddressUseCase updateAddressUseCase;
  private final CustomerDTOMapper customerDTOMapper;
  private final AddressDTOMapper addressDTOMapper;

  public CustomerController(CreateCustomerUseCase createCustomerUseCase,
                            GetCustomerByEmailUseCase getCustomerByEmailUseCase,
                            UpdateCustomerProfileUseCase updateCustomerProfileUseCase,
                            InsertAddressUseCase insertAddressUseCase,
                            UpdateAddressUseCase updateAddressUseCase,
                            CustomerDTOMapper customerDTOMapper,
                            AddressDTOMapper addressDTOMapper) {
    this.createCustomerUseCase = createCustomerUseCase;
    this.getCustomerByEmailUseCase = getCustomerByEmailUseCase;
    this.updateCustomerProfileUseCase = updateCustomerProfileUseCase;
    this.insertAddressUseCase = insertAddressUseCase;
    this.updateAddressUseCase = updateAddressUseCase;
    this.customerDTOMapper = customerDTOMapper;
    this.addressDTOMapper = addressDTOMapper;
  }

  @Override
  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<CustomerDTO> create(@Valid @RequestBody CreateCustomerDTO customerDTO) {
    Customer customer = this.customerDTOMapper.toDomain(customerDTO);
    Customer createdCustomer = this.createCustomerUseCase.execute(customer, customerDTO.password());

    return new ResponsePayload.Builder<CustomerDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Conta criada com sucesso")
      .payload(this.customerDTOMapper.toDTO(createdCustomer))
      .build();
  }

  @Override
  @GetMapping("/profile")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<CustomerProfileDTO> getAuthCustomerProfile(@AuthenticationPrincipal Jwt jwt,
                                                                    @RequestHeader("authCustomerEmail") String customerEmail) {
    final String email = customerEmail != null ? customerEmail : jwt.getSubject();
    Customer profile = this.getCustomerByEmailUseCase.execute(email);

    return new ResponsePayload.Builder<CustomerProfileDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Perfil do cliente autenticado")
      .payload(this.customerDTOMapper.toProfileDTO(profile))
      .build();
  }

  @Override
  @PatchMapping("/profile")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<CustomerProfileDTO> updateCustomerProfile(@AuthenticationPrincipal Jwt jwt,
                                                                   @RequestBody UpdateCustomerDTO customerDTO) {
    Customer updatedCustomer = this.updateCustomerProfileUseCase.execute(jwt.getSubject(), customerDTO);

    return new ResponsePayload.Builder<CustomerProfileDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Perfil atualizado com sucesso")
      .payload(this.customerDTOMapper.toProfileDTO(updatedCustomer))
      .build();
  }

  @Override
  @PostMapping("/profile/address")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<CustomerProfileDTO> insertAddress(@AuthenticationPrincipal Jwt jwt,
                                                           @Valid @RequestBody AddressDTO addressDTO) {
    Address address = this.addressDTOMapper.toDomain(addressDTO);
    Customer updatedCustomer = this.insertAddressUseCase.execute(jwt.getSubject(), address);

    return new ResponsePayload.Builder<CustomerProfileDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Endereço do cliente de email '" + updatedCustomer.getEmail() + "' inserido com sucesso")
      .payload(this.customerDTOMapper.toProfileDTO(updatedCustomer))
      .build();
  }

  @Override
  @PatchMapping("/profile/address")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<CustomerProfileDTO> updateAddress(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestBody AddressDTO addressDTO) {
    Address address = this.addressDTOMapper.toDomain(addressDTO);
    Customer updatedCustomer = this.updateAddressUseCase.execute(jwt.getSubject(), address);

    return new ResponsePayload.Builder<CustomerProfileDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Endereço do cliente de email '" + updatedCustomer.getEmail() + "' atualizado com sucesso")
      .payload(this.customerDTOMapper.toProfileDTO(updatedCustomer))
      .build();
  }
}
