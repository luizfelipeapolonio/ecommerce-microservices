package com.felipe.ecommerce_cart_service.infrastructure.gateway;

import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.infrastructure.external.InventoryService;
import com.felipe.ecommerce_cart_service.infrastructure.mappers.CartEntityMapper;
import com.felipe.ecommerce_cart_service.infrastructure.mappers.CartItemEntityMapper;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartEntity;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartItemEntity;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.repositories.CartItemRepository;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.repositories.CartRepository;
import com.felipe.response.ResponsePayload;
import com.felipe.response.product.ProductResponseDTO;
import com.felipe.utils.product.ProductUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class CartGatewayImpl implements CartGateway {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final CartEntityMapper cartEntityMapper;
  private final CartItemEntityMapper cartItemEntityMapper;
  private final InventoryService inventoryService;
  private static final Logger logger = LoggerFactory.getLogger(CartGatewayImpl.class);

  public CartGatewayImpl(CartRepository cartRepository,
                         CartItemRepository cartItemRepository,
                         CartEntityMapper cartEntityMapper,
                         InventoryService inventoryService,
                         CartItemEntityMapper cartItemEntityMapper) {
    this.cartRepository = cartRepository;
    this.cartItemRepository = cartItemRepository;
    this.cartEntityMapper = cartEntityMapper;
    this.inventoryService = inventoryService;
    this.cartItemEntityMapper = cartItemEntityMapper;
  }

  @Override
  public Cart createCart(UUID customerId) {
    final CartEntity entity = new CartEntity(customerId);
    return this.cartEntityMapper.toDomain(this.cartRepository.save(entity));
  }

  @Override
  public Optional<Cart> findCartByCustomerId(UUID customerId) {
    return this.cartRepository.findByCustomerId(customerId).map(this.cartEntityMapper::toDomain);
  }

  @Override
  public CartItem addItemToCart(Cart cart, UUID productId, int quantity) {
    final CartEntity cartEntity = this.cartEntityMapper.toEntity(cart);

    // Fetch product info
    final ResponsePayload<ProductResponseDTO> response = this.inventoryService.fetchProductById(productId);
    logger.info(
      "Inventory Service response in CartGatewayImpl -> status: {} - code: {} - message: {}",
      response.getType(), response.getCode(), response.getMessage()
    );

    final ProductResponseDTO product = response.getPayload();
    final BigDecimal finalPrice = product.withDiscount() ?
                                  ProductUtils.calculateFinalPrice(product.discountType(), product.unitPrice(), product.discountValue(), quantity) :
                                  ProductUtils.calculateFinalPrice(product.unitPrice(), quantity);

    final CartItemEntity cartItem = CartItemEntity.builder()
      .productId(productId)
      .productName(product.name())
      .thumbnailImage(product.images().getFirst().path())
      .unitPrice(new BigDecimal(product.unitPrice()))
      .discountType(product.discountType())
      .discountValue(product.discountValue())
      .finalPrice(finalPrice)
      .quantity(quantity)
      .cart(cartEntity)
      .build();

    final CartItemEntity savedCartItem = this.cartItemRepository.save(cartItem);
    return this.cartItemEntityMapper.toDomain(savedCartItem);
  }

  @Override
  public Optional<CartItem> findCartItemByProductIdAndCartId(UUID productId, UUID cartId) {
    return this.cartItemRepository.findByProductIdAndCartId(productId, cartId)
      .map(this.cartItemEntityMapper::toDomain);
  }
}