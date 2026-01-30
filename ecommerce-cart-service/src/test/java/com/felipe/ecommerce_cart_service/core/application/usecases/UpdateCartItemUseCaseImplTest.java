package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartItemNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.UpdateCartItemUseCaseImpl;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class UpdateCartItemUseCaseImplTest {
  @Mock
  private CartGateway cartGateway;

  @Mock
  private CustomerGateway customerGateway;

  private DataMock dataMock;
  private UpdateCartItemUseCaseImpl updateCartItemUseCase;
  private CustomerProfileDTO customerProfile;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.updateCartItemUseCase = new UpdateCartItemUseCaseImpl(this.cartGateway, this.customerGateway);
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
  @DisplayName("updateCartItemSuccess - Should successfully update a cart item and return it")
  void updateCartItemSuccess() {
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    cart.addItem(item);

    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());
    int quantity = 2;
    Long itemId = item.getId();
    String newFinalPrice = item.getUnitPrice()
      .multiply(new BigDecimal(quantity))
      .setScale(2, RoundingMode.HALF_DOWN)
      .toString();
    ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.of(cart));
    doNothing().when(this.cartGateway).updateCartItem(cartCaptor.capture());

    CartItem updatedItem = this.updateCartItemUseCase.execute(itemId, quantity, customerEmail);

    // Captor assertions
    CartItem captorItem = cartCaptor.getValue().getItems().getFirst();
    int captorItemQuantity = captorItem.getQuantity();
    String captorItemFinalPrice = captorItem.getFinalPrice().toString();

    assertThat(captorItemQuantity).isEqualTo(quantity);
    assertThat(captorItemFinalPrice).isEqualTo(newFinalPrice);

    // Return assertions
    assertThat(updatedItem.getId()).isEqualTo(item.getId());
    assertThat(updatedItem.getProductId()).isEqualTo(item.getProductId());
    assertThat(updatedItem.getProductName()).isEqualTo(item.getProductName());
    assertThat(updatedItem.getQuantity()).isEqualTo(quantity);
    assertThat(updatedItem.getThumbnailImage()).isEqualTo(item.getThumbnailImage());
    assertThat(updatedItem.getUnitPrice().toString()).isEqualTo(item.getUnitPrice().toString());
    assertThat(updatedItem.getDiscountType()).isEqualTo(item.getDiscountType());
    assertThat(updatedItem.getDiscountValue()).isEqualTo(item.getDiscountValue());
    assertThat(updatedItem.getFinalPrice().toString()).isEqualTo(newFinalPrice);
    assertThat(updatedItem.getAddedAt()).isEqualTo(item.getAddedAt());

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
    verify(this.cartGateway, times(1)).updateCartItem(cart);
  }

  @Test
  @DisplayName("updateCartItemFailsByCartNotFound - Should throw a CartNotFoundException if cart is not found")
  void updateCartItemFailsByCartNotFound() {
    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.updateCartItemUseCase.execute(1L, 1, customerEmail));

    assertThat(thrown)
      .isExactlyInstanceOf(CartNotFoundException.class)
      .hasMessage("Carrinho do cliente de id '%s' não encontrado", customerId);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
    verify(this.cartGateway, never()).updateCartItem(any(Cart.class));
  }

  @Test
  @DisplayName("updateCartItemFailsByCartItemNotFound - Should throw a CartItemNotFoundException if item is not in the cart")
  void updateCartItemFailsByCartItemNotFound() {
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    cart.addItem(this.dataMock.getCartItemsDomain().get(1));

    String customerEmail = this.customerProfile.email();
    UUID customerId = UUID.fromString(this.customerProfile.id());
    Long itemId = 1L;

    when(this.customerGateway.fetchAuthCustomerProfile(customerEmail)).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.of(cart));

    Exception thrown = catchException(() -> this.updateCartItemUseCase.execute(itemId, 1, customerEmail));

    assertThat(thrown)
      .isExactlyInstanceOf(CartItemNotFoundException.class)
      .hasMessage("Item de id '%d' não encontrado no carrinho", itemId);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(customerEmail);
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
    verify(this.cartGateway, never()).updateCartItem(any(Cart.class));
  }
}
