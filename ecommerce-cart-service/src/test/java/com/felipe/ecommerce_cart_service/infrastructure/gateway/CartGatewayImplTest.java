package com.felipe.ecommerce_cart_service.infrastructure.gateway;

import com.felipe.ecommerce_cart_service.core.application.dtos.CartItemDTO;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.infrastructure.external.InventoryService;
import com.felipe.ecommerce_cart_service.infrastructure.mappers.CartEntityMapper;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartEntity;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.entities.CartItemEntity;
import com.felipe.ecommerce_cart_service.infrastructure.persistence.repositories.CartRepository;
import com.felipe.ecommerce_cart_service.testutils.DataMock;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import com.felipe.response.product.ImageFileDTO;
import com.felipe.response.product.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartGatewayImplTest {
  @Mock
  private CartRepository cartRepository;

  @Mock
  private CartEntityMapper cartEntityMapper;

  @Mock
  private InventoryService inventoryService;

  @InjectMocks
  private CartGatewayImpl cartGateway;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createCartSuccess - Should successfully create a cart and return it")
  void createCartSuccess() {
    CartEntity cartEntity = this.dataMock.getCartsEntity().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    UUID customerId = UUID.fromString("6afa440a-0ac1-48ee-bc7c-b41954a23473");
    ArgumentCaptor<CartEntity> cartCaptor = ArgumentCaptor.forClass(CartEntity.class);

    when(this.cartRepository.save(cartCaptor.capture())).thenReturn(cartEntity);
    when(this.cartEntityMapper.toDomain(cartEntity)).thenReturn(cart);

    Cart createdCart = this.cartGateway.createCart(customerId);

    // Captor assertion
    assertThat(cartCaptor.getValue().getCustomerId()).isEqualTo(customerId);

    // Return assertion
    assertThat(createdCart.getId()).isEqualTo(cart.getId());
    assertThat(createdCart.getCustomerId()).isEqualTo(cart.getCustomerId());
    assertThat(createdCart.getCreatedAt()).isEqualTo(cart.getCreatedAt());

    verify(this.cartRepository, times(1)).save(any(CartEntity.class));
    verify(this.cartEntityMapper, times(1)).toDomain(cartEntity);
  }

  @Test
  @DisplayName("addItemToCartSuccess - Should successfully add an item to cart")
  void addItemToCartProductWithNoDiscountSuccess() {
    CartEntity cartEntity = this.dataMock.getCartsEntity().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    UUID productId = UUID.fromString("2e8db9ac-ffee-4f26-a387-ed4b298462e5");
    int quantity = 1;
    ArgumentCaptor<CartEntity> cartCaptor = ArgumentCaptor.forClass(CartEntity.class);
    ProductResponseDTO product = new ProductResponseDTO(
      "2e8db9ac-ffee-4f26-a387-ed4b298462e5",
      "Product 1",
      "Description",
      "120.00",
      1L,
      false,
      null,
      null,
      "",
      "",
      null,
      null,
      null,
      List.of(new ImageFileDTO(
        "",
        "",
        "/image-product1.jpg",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
      ))
    );
    String unitPrice = new BigDecimal(product.unitPrice()).toString();
    ResponsePayload<ProductResponseDTO> response = new ResponsePayload.Builder<ProductResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Successful response")
      .payload(product)
      .build();

    when(this.cartEntityMapper.toEntity(cart)).thenReturn(cartEntity);
    when(this.inventoryService.fetchProductById(productId)).thenReturn(response);
    when(this.cartRepository.save(cartCaptor.capture())).thenReturn(cartEntity);
    when(this.cartEntityMapper.toDomain(cartEntity)).thenReturn(cart);

    CartItemDTO addedItemDTO = this.cartGateway.addItemToCart(cart, productId, quantity);

    // Captor assertions
    assertThat(cartCaptor.getValue().getItems()).isNotEmpty();
    CartItemEntity capturedItem = cartCaptor.getValue().getItems().getFirst();
    assertThat(capturedItem.getProductId()).isEqualTo(productId);
    assertThat(capturedItem.getProductName()).isEqualTo(product.name());
    assertThat(capturedItem.getThumbnailImage()).isEqualTo(product.images().getFirst().path());
    assertThat(capturedItem.getUnitPrice().toString()).isEqualTo(unitPrice);
    assertThat(capturedItem.getDiscountType()).isEqualTo(product.discountType());
    assertThat(capturedItem.getDiscountValue()).isEqualTo(product.discountValue());
    assertThat(capturedItem.getFinalPrice().toString()).isEqualTo(unitPrice);
    assertThat(capturedItem.getQuantity()).isEqualTo(quantity);

    // Return assertions
    assertThat(addedItemDTO.itemIndex()).isEqualTo(0);
    assertThat(addedItemDTO.cart().getId()).isEqualTo(cart.getId());
    assertThat(addedItemDTO.cart().getCustomerId()).isEqualTo(cart.getCustomerId());
    assertThat(addedItemDTO.cart().getCreatedAt()).isEqualTo(cart.getCreatedAt());

    verify(this.cartEntityMapper, times(1)).toEntity(cart);
    verify(this.inventoryService, times(1)).fetchProductById(productId);
    verify(this.cartRepository, times(1)).save(cartEntity);
    verify(this.cartEntityMapper, times(1)).toDomain(cartEntity);
  }

  @Test
  @DisplayName("addItemToCartProductWithDiscountSuccess - Should successfully calculate the discount price and add the item to cart")
  void addItemToCartProductWithDiscountSuccess() {
    CartEntity cartEntity = this.dataMock.getCartsEntity().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    UUID productId = UUID.fromString("2e8db9ac-ffee-4f26-a387-ed4b298462e5");
    int quantity = 1;
    ArgumentCaptor<CartEntity> cartCaptor = ArgumentCaptor.forClass(CartEntity.class);
    ProductResponseDTO product = new ProductResponseDTO(
      "2e8db9ac-ffee-4f26-a387-ed4b298462e5",
      "Product 1",
      "Description",
      "120.00",
      1L,
      true,
      "fixed_amount",
      "20.00",
      "",
      "",
      null,
      null,
      null,
      List.of(new ImageFileDTO(
        "",
        "",
        "/image-product1.jpg",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
      ))
    );
    String unitPrice = new BigDecimal(product.unitPrice()).toString();
    String finalPrice = new BigDecimal(product.unitPrice())
      .multiply(new BigDecimal(quantity))
      .setScale(2, RoundingMode.HALF_DOWN)
      .subtract(new BigDecimal(product.discountValue()))
      .toString();
    ResponsePayload<ProductResponseDTO> response = new ResponsePayload.Builder<ProductResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Successful response")
      .payload(product)
      .build();

    when(this.cartEntityMapper.toEntity(cart)).thenReturn(cartEntity);
    when(this.inventoryService.fetchProductById(productId)).thenReturn(response);
    when(this.cartRepository.save(cartCaptor.capture())).thenReturn(cartEntity);
    when(this.cartEntityMapper.toDomain(cartEntity)).thenReturn(cart);

    CartItemDTO addedItemDTO = this.cartGateway.addItemToCart(cart, productId, quantity);

    // Captor assertions
    assertThat(cartCaptor.getValue().getItems()).isNotEmpty();
    CartItemEntity capturedItem = cartCaptor.getValue().getItems().getFirst();
    assertThat(capturedItem.getProductId()).isEqualTo(productId);
    assertThat(capturedItem.getProductName()).isEqualTo(product.name());
    assertThat(capturedItem.getThumbnailImage()).isEqualTo(product.images().getFirst().path());
    assertThat(capturedItem.getUnitPrice().toString()).isEqualTo(unitPrice);
    assertThat(capturedItem.getDiscountType()).isEqualTo(product.discountType());
    assertThat(capturedItem.getDiscountValue()).isEqualTo(product.discountValue());
    assertThat(capturedItem.getFinalPrice().toString()).isEqualTo(finalPrice);
    assertThat(capturedItem.getQuantity()).isEqualTo(quantity);

    // Return assertions
    assertThat(addedItemDTO.itemIndex()).isEqualTo(0);
    assertThat(addedItemDTO.cart().getId()).isEqualTo(cart.getId());
    assertThat(addedItemDTO.cart().getCustomerId()).isEqualTo(cart.getCustomerId());
    assertThat(addedItemDTO.cart().getCreatedAt()).isEqualTo(cart.getCreatedAt());

    verify(this.cartEntityMapper, times(1)).toEntity(cart);
    verify(this.inventoryService, times(1)).fetchProductById(productId);
    verify(this.cartRepository, times(1)).save(cartEntity);
    verify(this.cartEntityMapper, times(1)).toDomain(cartEntity);
  }

  @Test
  @DisplayName("removeItemFromCartSuccess - Should successfully remove an item from cart")
  void removeItemFromCartSuccess() {
    CartEntity cartEntity = this.dataMock.getCartsEntity().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();

    when(this.cartEntityMapper.toEntity(cart)).thenReturn(cartEntity);
    when(this.cartRepository.save(cartEntity)).thenReturn(cartEntity);

    this.cartGateway.removeItemFromCart(cart);

    verify(this.cartEntityMapper, times(1)).toEntity(cart);
    verify(this.cartRepository, times(1)).save(cartEntity);
  }

  @Test
  @DisplayName("updateItemSuccess - Should successfully update an item")
  void updateItemSuccess() {
    CartEntity cartEntity = this.dataMock.getCartsEntity().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();

    when(this.cartEntityMapper.toEntity(cart)).thenReturn(cartEntity);
    when(this.cartRepository.save(cartEntity)).thenReturn(cartEntity);

    this.cartGateway.updateCartItem(cart);

    verify(this.cartEntityMapper, times(1)).toEntity(cart);
    verify(this.cartRepository, times(1)).save(cartEntity);
  }

  @Test
  @DisplayName("findCartByCustomerIdSuccess - Should find a cart by customer id and return an Optional of Cart")
  void findCartByCustomerIdSuccess() {
    CartEntity cartEntity = this.dataMock.getCartsEntity().getFirst();
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    UUID customerId = cart.getCustomerId();

    when(this.cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cartEntity));
    when(this.cartEntityMapper.toDomain(cartEntity)).thenReturn(cart);

    Optional<Cart> foundCart = this.cartGateway.findCartByCustomerId(customerId);

    assertThat(foundCart.isPresent()).isTrue();
    assertThat(foundCart.get().getId()).isEqualTo(cart.getId());
    assertThat(foundCart.get().getCustomerId()).isEqualTo(cart.getCustomerId());
    assertThat(foundCart.get().getCreatedAt()).isEqualTo(cart.getCreatedAt());
    assertThat(foundCart.get().getItems().size()).isEqualTo(cart.getItems().size());

    verify(this.cartRepository, times(1)).findByCustomerId(customerId);
    verify(this.cartEntityMapper, times(1)).toDomain(cartEntity);
  }
}
