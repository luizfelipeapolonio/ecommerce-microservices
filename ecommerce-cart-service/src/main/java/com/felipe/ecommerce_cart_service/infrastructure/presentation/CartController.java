package com.felipe.ecommerce_cart_service.infrastructure.presentation;

import com.felipe.ecommerce_cart_service.core.application.usecases.AddItemToCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.CreateCartUseCase;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.AddItemToCartDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CartItemResponseDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CartResponseDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CreateCartDTO;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
  private final CreateCartUseCase createCartUseCase;
  private final AddItemToCartUseCase addItemToCartUseCase;

  public CartController(CreateCartUseCase createCartUseCase, AddItemToCartUseCase addItemToCartUseCase) {
    this.createCartUseCase = createCartUseCase;
    this.addItemToCartUseCase = addItemToCartUseCase;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<CartResponseDTO> createCart(@Valid @RequestBody CreateCartDTO cartDTO) {
    final Cart createdCart = this.createCartUseCase.execute(cartDTO.customerId());
    return new ResponsePayload.Builder<CartResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Carrinho do usuário de id '" + createdCart.getCustomerId()  + "' criado com sucesso")
      .payload(new CartResponseDTO(createdCart))
      .build();
  }

  @PostMapping("/items")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<CartItemResponseDTO> addItemToCart(@AuthenticationPrincipal Jwt jwt,
                                                    @Valid @RequestBody AddItemToCartDTO itemDTO) {
    final CartItem addedItem = this.addItemToCartUseCase.execute(itemDTO.productId(), itemDTO.quantity(), jwt.getSubject());
    final String message = String.format(
      "Item '%s' adicionado com sucesso no carrinho de id '%s'",
      addedItem.getProductName(), addedItem.getCart().getId()
    );

    return new ResponsePayload.Builder<CartItemResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message(message)
      .payload(new CartItemResponseDTO(addedItem))
      .build();
  }
}
