package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartItemNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.GetCartItemByIdUseCaseImpl;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCartItemByIdUseCaseImplTest {
  @Mock
  private CartGateway cartGateway;

  @Mock
  private CustomerGateway customerGateway;

  private DataMock dataMock;
  private GetCartItemByIdUseCaseImpl getCartItemByIdUseCase;
  private CustomerProfileDTO customerProfile;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.getCartItemByIdUseCase = new GetCartItemByIdUseCaseImpl(this.cartGateway, this.customerGateway);
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
  @DisplayName("getCartItemByIdSuccess - Should find a cart item by id and return it")
  void getCartItemByIdSuccess() {
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    cart.addItem(item);

    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());
    Long itemId = 1L;

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.of(cart));

    CartItem foundItem = this.getCartItemByIdUseCase.execute(itemId, customerEmail);

    assertThat(foundItem.getId()).isEqualTo(item.getId());
    assertThat(foundItem.getProductId()).isEqualTo(item.getProductId());
    assertThat(foundItem.getProductName()).isEqualTo(item.getProductName());
    assertThat(foundItem.getThumbnailImage()).isEqualTo(item.getThumbnailImage());
    assertThat(foundItem.getUnitPrice().toString()).isEqualTo(item.getUnitPrice().toString());
    assertThat(foundItem.getQuantity()).isEqualTo(item.getQuantity());
    assertThat(foundItem.getDiscountType()).isEqualTo(item.getDiscountType());
    assertThat(foundItem.getDiscountValue()).isEqualTo(item.getDiscountValue());
    assertThat(foundItem.getFinalPrice().toString()).isEqualTo(item.getFinalPrice().toString());
    assertThat(foundItem.getAddedAt()).isEqualTo(item.getAddedAt());

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
  }

  @Test
  @DisplayName("getCartItemByIdFailsByCartNotFound - Should throw a CartNotFoundException if cart is not found")
  void getCartItemByIdFailsByCartNotFound() {
    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getCartItemByIdUseCase.execute(1L, customerEmail));

    assertThat(thrown)
      .isExactlyInstanceOf(CartNotFoundException.class)
      .hasMessage("Carrinho do cliente de id '%s' não encontrado", customerId);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
  }

  @Test
  @DisplayName("getCartItemByIdFailsByItemNotFound - Should throw a CartItemNotFoundException if item is not in the cart")
  void getCartItemByIdFailsByItemNotFound() {
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.of(cart));

    Exception thrown = catchException(() -> this.getCartItemByIdUseCase.execute(1L, customerEmail));

    assertThat(thrown)
      .isExactlyInstanceOf(CartItemNotFoundException.class)
      .hasMessage("Item de id '%d' não encontrado no carrinho", 1L);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
  }
}
