package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.GetAllCartItemsUseCaseImpl;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllCartItemsUseCaseImplTest {
  @Mock
  private CartGateway cartGateway;

  @Mock
  private CustomerGateway customerGateway;

  private DataMock dataMock;
  private CustomerProfileDTO customerProfile;
  private GetAllCartItemsUseCaseImpl getAllCartItemsUseCase;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.getAllCartItemsUseCase = new GetAllCartItemsUseCaseImpl(this.cartGateway, this.customerGateway);
    this.customerProfile = new CustomerProfileDTO(
      "a963a94b-7dda-4f30-aa39-4afee21e7efe",
      "customer1@email.com",
      "johndoe",
      "John",
      "Doe",
      "",
      "",
      null
    );
  }

  @Test
  @DisplayName("getAllCartItemsSuccess - Should successfully find and return all cart items")
  void getAllCartItemsSuccess() {
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    cart.addItem(this.dataMock.getCartItemsDomain().getFirst());
    cart.addItem(this.dataMock.getCartItemsDomain().get(1));

    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.of(cart));

    List<CartItem> items = this.getAllCartItemsUseCase.execute(customerEmail);

    assertThat(items).isNotEmpty();
    assertThat(items.size()).isEqualTo(cart.getItems().size());

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
  }

  @Test
  @DisplayName("getAllCartItemsFailsByCartNotFound - Should throw a CartNotFoundException if cart is not found")
  void getAllCartItemsFailsByCartNotFound() {
    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getAllCartItemsUseCase.execute(customerEmail));

    assertThat(thrown)
      .isExactlyInstanceOf(CartNotFoundException.class)
      .hasMessage("Carrinho do cliente de id '%s' não encontrado", customerId);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
  }
}
