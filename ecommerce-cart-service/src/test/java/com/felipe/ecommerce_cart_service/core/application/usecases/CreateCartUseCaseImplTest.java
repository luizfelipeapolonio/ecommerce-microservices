package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.impl.CreateCartUseCaseImpl;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCartUseCaseImplTest {
  @Mock
  private CartGateway cartGateway;

  private DataMock dataMock;
  private CreateCartUseCaseImpl createCartUseCaseImpl;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.createCartUseCaseImpl = new CreateCartUseCaseImpl(this.cartGateway);
  }

  @Test
  @DisplayName("createCartSuccess - Should successfully create a cart and return it")
  void createCartSuccess() {
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    UUID customerId = cart.getCustomerId();

    when(this.cartGateway.createCart(customerId)).thenReturn(cart);

    Cart createdCart = this.createCartUseCaseImpl.execute(customerId);

    assertThat(createdCart.getId()).isEqualTo(cart.getId());
    assertThat(createdCart.getCustomerId()).isEqualTo(cart.getCustomerId());
    assertThat(createdCart.getCreatedAt()).isEqualTo(cart.getCreatedAt());

    verify(this.cartGateway, times(1)).createCart(customerId);
  }
}
