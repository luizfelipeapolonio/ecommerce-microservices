package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartItemNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.RemoveItemFromCartUseCaseImpl;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveItemFromCartUseCaseImplTest {
  @Mock
  private CartGateway cartGateway;

  @Mock
  private CustomerGateway customerGateway;

  private DataMock dataMock;
  private RemoveItemFromCartUseCaseImpl removeItemFromCartUseCase;
  private CustomerProfileDTO customerProfile;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.removeItemFromCartUseCase = new RemoveItemFromCartUseCaseImpl(this.cartGateway, this.customerGateway);
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
  @DisplayName("removeItemFromCartSuccess - Should successfully remove an item from cart and return it")
  void removeItemFromCartSuccess() {
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    cart.addItem(item);

    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());
    Long itemId = item.getId();
    ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.of(cart));
    doNothing().when(this.cartGateway).removeItemFromCart(cartCaptor.capture());

    CartItem removedItem = this.removeItemFromCartUseCase.execute(itemId, customerEmail);

    // Captor assertion
    assertThat(cartCaptor.getValue().getItems().size()).isEqualTo(0);

    // Return assertion
    assertThat(removedItem.getId()).isEqualTo(item.getId());
    assertThat(removedItem.getProductId()).isEqualTo(item.getProductId());
    assertThat(removedItem.getProductName()).isEqualTo(item.getProductName());
    assertThat(removedItem.getThumbnailImage()).isEqualTo(item.getThumbnailImage());
    assertThat(removedItem.getUnitPrice().toString()).isEqualTo(item.getUnitPrice().toString());
    assertThat(removedItem.getQuantity()).isEqualTo(item.getQuantity());
    assertThat(removedItem.getDiscountType()).isEqualTo(item.getDiscountType());
    assertThat(removedItem.getDiscountValue()).isEqualTo(item.getDiscountValue());
    assertThat(removedItem.getFinalPrice()).isEqualTo(item.getFinalPrice());
    assertThat(removedItem.getAddedAt()).isEqualTo(item.getAddedAt());

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).removeItemFromCart(cart);
  }

  @Test
  @DisplayName("removeItemFromCartFailsByCartNotFound - Should throw a CartNotFoundException if cart is not found")
  void removeItemFromCartFailsByCartNotFound() {
    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.removeItemFromCartUseCase.execute(1L, customerEmail));

    assertThat(thrown)
      .isExactlyInstanceOf(CartNotFoundException.class)
      .hasMessage("Carrinho do cliente de id '%s' não encontrado", customerId);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
    verify(this.cartGateway, never()).removeItemFromCart(any(Cart.class));
  }

  @Test
  @DisplayName("removeItemFromCartFailsByItemNotFound - Should throw a CartItemNotFoundException if tem is not in the cart")
  void removeItemFromCartFailsByItemNotFound() {
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.of(cart));

    Exception thrown = catchException(() -> this.removeItemFromCartUseCase.execute(1L, customerEmail));

    assertThat(thrown)
      .isExactlyInstanceOf(CartItemNotFoundException.class)
      .hasMessage("Item de id '%d' não encontrado no carrinho", 1);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
    verify(this.cartGateway, never()).removeItemFromCart(any(Cart.class));
  }
}
