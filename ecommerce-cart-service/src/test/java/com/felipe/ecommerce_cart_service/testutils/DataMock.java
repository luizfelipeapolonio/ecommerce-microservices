package com.felipe.ecommerce_cart_service.testutils;

import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartEntity;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartItemEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataMock {
  private final List<Cart> cartsDomain = new ArrayList<>();
  private final List<CartItem> cartItemsDomain = new ArrayList<>();
  private final List<CartEntity> cartsEntity = new ArrayList<>();
  private final List<CartItemEntity> cartItemsEntity = new ArrayList<>();
  private static final LocalDateTime MOCK_DATETIME = LocalDateTime.parse("2026-01-29T22:13:05.116661297");

  public DataMock() {
    createCartsDomain();
    createCartItemsDomain();
    createCartsEntity();
    createCartItemsEntity();
  }

  public List<Cart> getCartsDomain() {
    return this.cartsDomain;
  }

  public List<CartItem> getCartItemsDomain() {
    return this.cartItemsDomain;
  }

  public List<CartEntity> getCartsEntity() {
    return this.cartsEntity;
  }

  public List<CartItemEntity> getCartItemsEntity() {
    return this.cartItemsEntity;
  }

  private void createCartsDomain() {
    Cart cart1 = new Cart();
    cart1.setId(UUID.fromString("44461b57-de67-47ad-a8f0-5b8bde050055"));
    cart1.setCustomerId(UUID.fromString("a963a94b-7dda-4f30-aa39-4afee21e7efe"));
    cart1.setCreatedAt(MOCK_DATETIME);

    Cart cart2 = new Cart();
    cart2.setId(UUID.fromString("e223dc05-3618-4156-9435-59d9c38c3212"));
    cart2.setCustomerId(UUID.fromString("6afa440a-0ac1-48ee-bc7c-b41954a23473"));
    cart2.setCreatedAt(MOCK_DATETIME);

    this.cartsDomain.add(cart1);
    this.cartsDomain.add(cart2);
  }

  private void createCartItemsDomain() {
    CartItem cartItem1 = new CartItem()
      .id(1L)
      .productId(UUID.fromString("2e8db9ac-ffee-4f26-a387-ed4b298462e5"))
      .productName("Product 1")
      .thumbnailImage("/image-product1.jpg")
      .unitPrice(new BigDecimal("120.00"))
      .discountType(null)
      .discountValue(null)
      .finalPrice(new BigDecimal("120.00"))
      .quantity(1)
      .addedAt(MOCK_DATETIME);

    CartItem cartItem2 = new CartItem()
      .id(2L)
      .productId(UUID.fromString("bb121f24-891c-4b82-974c-2074ca87298f"))
      .productName("Product 2")
      .thumbnailImage("/image-product2.jpg")
      .unitPrice(new BigDecimal("200.00"))
      .discountType("fixed_amount")
      .discountValue("20.00")
      .finalPrice(new BigDecimal("180.00"))
      .quantity(1)
      .addedAt(MOCK_DATETIME);

    CartItem cartItem3 = new CartItem()
      .id(3L)
      .productId(UUID.fromString("c8f499ea-034f-4ac5-9d70-353ebc1f8ce8"))
      .productName("Product 3")
      .thumbnailImage("/image-product3.jpg")
      .unitPrice(new BigDecimal("150.00"))
      .discountType("percentage")
      .discountValue("10.00")
      .finalPrice(new BigDecimal("165.00"))
      .quantity(1)
      .addedAt(MOCK_DATETIME);

    this.cartItemsDomain.add(cartItem1);
    this.cartItemsDomain.add(cartItem2);
    this.cartItemsDomain.add(cartItem3);
  }

  private void createCartsEntity() {
    CartEntity cart1 = new CartEntity();
    cart1.setId(UUID.fromString("44461b57-de67-47ad-a8f0-5b8bde050055"));
    cart1.setCustomerId(UUID.fromString("a963a94b-7dda-4f30-aa39-4afee21e7efe"));
    cart1.setCreatedAt(MOCK_DATETIME);

    CartEntity cart2 = new CartEntity();
    cart2.setId(UUID.fromString("e223dc05-3618-4156-9435-59d9c38c3212"));
    cart2.setCustomerId(UUID.fromString("6afa440a-0ac1-48ee-bc7c-b41954a23473"));
    cart2.setCreatedAt(MOCK_DATETIME);

    this.cartsEntity.add(cart1);
    this.cartsEntity.add(cart2);
  }

  private void createCartItemsEntity() {
    CartItemEntity cartItem1 = new CartItemEntity()
      .id(1L)
      .productId(UUID.fromString("2e8db9ac-ffee-4f26-a387-ed4b298462e5"))
      .productName("Product 1")
      .thumbnailImage("/image-product1.jpg")
      .unitPrice(new BigDecimal("120.00"))
      .discountType(null)
      .discountValue(null)
      .finalPrice(new BigDecimal("120.00"))
      .quantity(1)
      .addedAt(MOCK_DATETIME);

    CartItemEntity cartItem2 = new CartItemEntity()
      .id(2L)
      .productId(UUID.fromString("bb121f24-891c-4b82-974c-2074ca87298f"))
      .productName("Product 2")
      .thumbnailImage("/image-product2.jpg")
      .unitPrice(new BigDecimal("200.00"))
      .discountType("fixed_amount")
      .discountValue("20.00")
      .finalPrice(new BigDecimal("180.00"))
      .quantity(1)
      .addedAt(MOCK_DATETIME);

    CartItemEntity cartItem3 = new CartItemEntity()
      .id(3L)
      .productId(UUID.fromString("c8f499ea-034f-4ac5-9d70-353ebc1f8ce8"))
      .productName("Product 3")
      .thumbnailImage("/image-product3.jpg")
      .unitPrice(new BigDecimal("150.00"))
      .discountType("percentage")
      .discountValue("10.00")
      .finalPrice(new BigDecimal("165.00"))
      .quantity(1)
      .addedAt(MOCK_DATETIME);

    this.cartItemsEntity.add(cartItem1);
    this.cartItemsEntity.add(cartItem2);
    this.cartItemsEntity.add(cartItem3);
  }
}
