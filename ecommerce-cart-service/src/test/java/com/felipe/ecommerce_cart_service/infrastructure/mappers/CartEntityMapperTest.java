package com.felipe.ecommerce_cart_service.infrastructure.mappers;

import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartEntity;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartItemEntity;
import com.felipe.ecommerce_cart_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartEntityMapperTest {

  @Spy
  private CartEntityMapper cartEntityMapper;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("convertCartEntityToDomain - Should successfully convert a CartEntity to Cart")
  void convertCartEntityToDomain() {
    CartItemEntity cartItemEntity = this.dataMock.getCartItemsEntity().getFirst();
    CartEntity cartEntity = this.dataMock.getCartsEntity().getFirst();
    cartEntity.addItem(cartItemEntity);

    Cart convertedCart = this.cartEntityMapper.toDomain(cartEntity);

    // Cart assertions
    assertThat(convertedCart.getId()).isEqualTo(cartEntity.getId());
    assertThat(convertedCart.getCustomerId()).isEqualTo(cartEntity.getCustomerId());
    assertThat(convertedCart.getCreatedAt()).isEqualTo(cartEntity.getCreatedAt());

    // Cart item assertions
    CartItem cartItem = convertedCart.getItems().getFirst();
    assertThat(cartItem.getId()).isEqualTo(cartItemEntity.getId());
    assertThat(cartItem.getProductId()).isEqualTo(cartItemEntity.getProductId());
    assertThat(cartItem.getProductName()).isEqualTo(cartItemEntity.getProductName());
    assertThat(cartItem.getThumbnailImage()).isEqualTo(cartItemEntity.getThumbnailImage());
    assertThat(cartItem.getUnitPrice().toString()).isEqualTo(cartItemEntity.getUnitPrice().toString());
    assertThat(cartItem.getDiscountType()).isEqualTo(cartItemEntity.getDiscountType());
    assertThat(cartItem.getDiscountValue()).isEqualTo(cartItemEntity.getDiscountValue());
    assertThat(cartItem.getFinalPrice().toString()).isEqualTo(cartItemEntity.getFinalPrice().toString());
    assertThat(cartItem.getQuantity()).isEqualTo(cartItemEntity.getQuantity());
    assertThat(cartItem.getCart()).isNotNull();

    verify(this.cartEntityMapper, times(1)).toDomain(cartEntity);
  }

  @Test
  @DisplayName("convertCartDomainToCartEntity - Should successfully convert a Cart to CartEntity")
  void convertCartDomainToCartEntity() {
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    cart.addItem(item);

    CartEntity convertedCartEntity = this.cartEntityMapper.toEntity(cart);

    // Cart assertions
    assertThat(convertedCartEntity.getId()).isEqualTo(cart.getId());
    assertThat(convertedCartEntity.getCustomerId()).isEqualTo(cart.getCustomerId());
    assertThat(convertedCartEntity.getCreatedAt()).isEqualTo(cart.getCreatedAt());

    // Cart item assertions
    CartItemEntity cartItem = convertedCartEntity.getItems().getFirst();
    assertThat(cartItem.getId()).isEqualTo(item.getId());
    assertThat(cartItem.getProductId()).isEqualTo(item.getProductId());
    assertThat(cartItem.getProductName()).isEqualTo(item.getProductName());
    assertThat(cartItem.getThumbnailImage()).isEqualTo(item.getThumbnailImage());
    assertThat(cartItem.getUnitPrice().toString()).isEqualTo(item.getUnitPrice().toString());
    assertThat(cartItem.getDiscountType()).isEqualTo(item.getDiscountType());
    assertThat(cartItem.getDiscountValue()).isEqualTo(item.getDiscountValue());
    assertThat(cartItem.getFinalPrice().toString()).isEqualTo(item.getFinalPrice().toString());
    assertThat(cartItem.getQuantity()).isEqualTo(item.getQuantity());
    assertThat(cartItem.getCart()).isNotNull();

    verify(this.cartEntityMapper, times(1)).toEntity(cart);
  }
}
