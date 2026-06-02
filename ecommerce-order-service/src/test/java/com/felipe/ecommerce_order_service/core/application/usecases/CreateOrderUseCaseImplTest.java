package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderProductDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.exceptions.CustomerAddressNotDefinedException;
import com.felipe.ecommerce_order_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.impl.CreateOrderUseCaseImpl;
import com.felipe.ecommerce_order_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseImplTest {

  @Mock
  private OrderGateway orderGateway;

  @Mock
  private CustomerGateway customerGateway;

  @Mock
  private CouponGateway couponGateway;

  private CreateOrderUseCase createOrderUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.createOrderUseCase = new CreateOrderUseCaseImpl(this.orderGateway, this.customerGateway, this.couponGateway);
  }

  @Test
  @DisplayName("createOrderWithCouponCodeSuccess - Should successfully create an order")
  void createOrderWithCouponCodeSuccess() {
    CustomerProfileDTO customerProfile = this.dataMock.getCustomerProfileDTO();
    UUID customerId = UUID.fromString(customerProfile.id());
    UUID sagaId = UUID.fromString("95480447-11bd-4b55-a0af-821339812588");
    UUID orderId = UUID.fromString("ea22196e-157a-4ff5-8fbd-2f1508651b4d");
    CreateOrderDTOImpl createOrderDTO = new CreateOrderDTOImpl(List.of(
      new CreateOrderProductDTOImpl(UUID.fromString("b244f4e2-21ea-43fe-ac42-e09679036acb"), 1L)
    ), "COUPON20");
    Map<String, UUID> response = Map.of(
      "orderId", orderId,
      "sagaId", sagaId
    );

    when(this.customerGateway.fetchAuthCustomerProfile(customerProfile.email())).thenReturn(customerProfile);
    doNothing().when(this.couponGateway).checkIfCouponIsValid(createOrderDTO.couponCode());
    when(this.orderGateway.createOrder(customerId, createOrderDTO)).thenReturn(response);

    Map<String, UUID> createOrderResponse = this.createOrderUseCase.execute(createOrderDTO, customerProfile.email());

    assertThat(createOrderResponse).containsKeys("orderId", "sagaId");
    assertThat(createOrderResponse).containsValues(orderId, sagaId);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerProfile.email());
    verify(this.couponGateway, times(1)).checkIfCouponIsValid(createOrderDTO.couponCode());
    verify(this.orderGateway, times(1)).createOrder(customerId, createOrderDTO);
  }

  @Test
  @DisplayName("createOrderWithNoCouponCodeSuccess - Should successfully create an order")
  void createOrderWithNoCouponCodeSuccess() {
    CustomerProfileDTO customerProfile = this.dataMock.getCustomerProfileDTO();
    UUID customerId = UUID.fromString(customerProfile.id());
    UUID sagaId = UUID.fromString("95480447-11bd-4b55-a0af-821339812588");
    UUID orderId = UUID.fromString("ea22196e-157a-4ff5-8fbd-2f1508651b4d");
    CreateOrderDTOImpl createOrderDTO = new CreateOrderDTOImpl(List.of(
      new CreateOrderProductDTOImpl(UUID.fromString("b244f4e2-21ea-43fe-ac42-e09679036acb"), 1L)
    ), null);
    Map<String, UUID> response = Map.of(
      "orderId", orderId,
      "sagaId", sagaId
    );

    when(this.customerGateway.fetchAuthCustomerProfile(customerProfile.email())).thenReturn(customerProfile);
    when(this.orderGateway.createOrder(customerId, createOrderDTO)).thenReturn(response);

    Map<String, UUID> createOrderResponse = this.createOrderUseCase.execute(createOrderDTO, customerProfile.email());

    assertThat(createOrderResponse).containsKeys("orderId", "sagaId");
    assertThat(createOrderResponse).containsValues(orderId, sagaId);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerProfile.email());
    verify(this.couponGateway, never()).checkIfCouponIsValid(anyString());
    verify(this.orderGateway, times(1)).createOrder(customerId, createOrderDTO);
  }

  @Test
  @DisplayName("createOrderFailsByCustomerAddressNull - Should throw a CustomerAddressNotDefinedException if customer address is null")
  void createOrderFailsByCustomerAddressNull() {
    CustomerProfileDTO customerProfile = customerProfileWithNoAddress(this.dataMock.getCustomerProfileDTO());

    when(this.customerGateway.fetchAuthCustomerProfile(customerProfile.email())).thenReturn(customerProfile);

    Exception thrown = catchException(() -> this.createOrderUseCase.execute(null, customerProfile.email()));

    assertThat(thrown)
      .isExactlyInstanceOf(CustomerAddressNotDefinedException.class)
      .hasMessage("Cliente de id '%s' não possui um endereço definido.", customerProfile.id());

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerProfile.email());
    verify(this.couponGateway, never()).checkIfCouponIsValid(anyString());
    verify(this.orderGateway, never()).createOrder(any(UUID.class), any(CreateOrderDTO.class));
  }

  private record CreateOrderDTOImpl(List<CreateOrderProductDTOImpl> products, String couponCode) implements CreateOrderDTO {}
  private record CreateOrderProductDTOImpl(UUID id, long quantity) implements CreateOrderProductDTO {}

  private CustomerProfileDTO customerProfileWithNoAddress(CustomerProfileDTO customerProfile) {
    return new CustomerProfileDTO(
      customerProfile.id(),
      customerProfile.email(),
      customerProfile.username(),
      customerProfile.firstName(),
      customerProfile.lastName(),
      customerProfile.createdAt(),
      customerProfile.updatedAt(),
      null
    );
  }
}