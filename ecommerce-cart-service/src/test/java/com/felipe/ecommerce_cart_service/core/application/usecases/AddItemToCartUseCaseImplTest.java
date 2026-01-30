package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.application.dtos.CartItemDTO;
import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartItemAlreadyExistsException;
import com.felipe.ecommerce_cart_service.core.application.exceptions.CartNotFoundException;
import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.AddItemToCartUseCaseImpl;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;

@ExtendWith(MockitoExtension.class)
public class AddItemToCartUseCaseImplTest {
  @Mock
  private CartGateway cartGateway;

  @Mock
  private CustomerGateway customerGateway;

  private DataMock dataMock;
  private CustomerProfileDTO customerProfile;
  private AddItemToCartUseCaseImpl addItemToCartUseCase;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.addItemToCartUseCase = new AddItemToCartUseCaseImpl(this.cartGateway, this.customerGateway);
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
  @DisplayName("addItemToCartSuccess - Should successfully add an item to cart and return it")
  void addItemToCartSuccess() {
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();
    UUID productId = item.getProductId();
    int quantity = 1;

    Cart cartWithItem = new Cart();
    cartWithItem.setId(cart.getId());
    cartWithItem.setCustomerId(cart.getCustomerId());
    cartWithItem.setCreatedAt(cart.getCreatedAt());
    cartWithItem.addItem(item);

    UUID customerId = UUID.fromString(this.customerProfile.id());
    CartItemDTO cartItemDTO = new CartItemDTO(0, cartWithItem);

    when(this.customerGateway.fetchAuthCustomerProfile(this.customerProfile.email())).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.of(cart));
    when(this.cartGateway.addItemToCart(cart, productId, quantity)).thenReturn(cartItemDTO);

    CartItem addedCartItem = this.addItemToCartUseCase.execute(productId, quantity, this.customerProfile.email());

    assertThat(addedCartItem.getId()).isEqualTo(item.getId());
    assertThat(addedCartItem.getProductId()).isEqualTo(item.getProductId());
    assertThat(addedCartItem.getProductName()).isEqualTo(item.getProductName());
    assertThat(addedCartItem.getQuantity()).isEqualTo(item.getQuantity());
    assertThat(addedCartItem.getThumbnailImage()).isEqualTo(item.getThumbnailImage());
    assertThat(addedCartItem.getUnitPrice().toString()).isEqualTo(item.getUnitPrice().toString());
    assertThat(addedCartItem.getDiscountType()).isEqualTo(item.getDiscountType());
    assertThat(addedCartItem.getDiscountValue()).isEqualTo(item.getDiscountValue());
    assertThat(addedCartItem.getFinalPrice()).isEqualTo(item.getFinalPrice());
    assertThat(addedCartItem.getAddedAt()).isEqualTo(item.getAddedAt());
    assertThat(addedCartItem.getCart().getId()).isEqualTo(cartWithItem.getId());

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(this.customerProfile.email());
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
    verify(this.cartGateway, times(1)).addItemToCart(cart, productId, quantity);
  }

  @Test
  @DisplayName("addItemToCartFailsByCartNotFound - Should throw a CartNotFoundException if cart is not found")
  void addItemToCartFailsByCartNotFound() {
    UUID customerId = UUID.fromString(this.customerProfile.id());
    UUID productId = this.dataMock.getCartItemsDomain().getFirst().getProductId();

    when(this.customerGateway.fetchAuthCustomerProfile(this.customerProfile.email())).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.addItemToCartUseCase.execute(productId, 1, this.customerProfile.email()));

    assertThat(thrown)
      .isExactlyInstanceOf(CartNotFoundException.class)
      .hasMessage("Carrinho do cliente de id '%s' não encontrado", customerId);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(this.customerProfile.email());
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
    verify(this.cartGateway, never()).addItemToCart(any(Cart.class), any(UUID.class), anyInt());
  }

  @Test
  @DisplayName("addItemToCartFailsByCartIsAlreadyInCart - Should throw a CartItemAlreadyExists if item is already in the cart")
  void addItemToCartFailsByCartIsAlreadyInCart() {
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    cart.addItem(item);

    UUID customerId = UUID.fromString(this.customerProfile.id());
    UUID productId = item.getProductId();

    when(this.customerGateway.fetchAuthCustomerProfile(this.customerProfile.email())).thenReturn(this.customerProfile);
    when(this.cartGateway.findCartByCustomerId(customerId)).thenReturn(Optional.of(cart));

    Exception thrown = catchException(() -> this.addItemToCartUseCase.execute(productId, 1, this.customerProfile.email()));

    assertThat(thrown)
      .isExactlyInstanceOf(CartItemAlreadyExistsException.class)
      .hasMessage("O produto de id '%s' já está no carrinho", productId);

    verify(this.customerGateway, times(1)).fetchAuthCustomerProfile(this.customerProfile.email());
    verify(this.cartGateway, times(1)).findCartByCustomerId(customerId);
    verify(this.cartGateway, never()).addItemToCart(any(Cart.class), any(UUID.class), anyInt());
  }
}
