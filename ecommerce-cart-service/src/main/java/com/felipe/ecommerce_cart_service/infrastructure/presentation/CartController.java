package com.felipe.ecommerce_cart_service.infrastructure.presentation;

import com.felipe.ecommerce_cart_service.core.application.usecases.AddItemToCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.CreateCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.GetAllCartItemsUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.GetCartItemByIdUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.RemoveItemFromCartUseCase;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
  private final CreateCartUseCase createCartUseCase;
  private final AddItemToCartUseCase addItemToCartUseCase;
  private final RemoveItemFromCartUseCase removeItemFromCartUseCase;
  private final GetCartItemByIdUseCase getCartItemByIdUseCase;
  private final GetAllCartItemsUseCase getAllCartItemsUseCase;

  public CartController(CreateCartUseCase createCartUseCase,
                        AddItemToCartUseCase addItemToCartUseCase,
                        RemoveItemFromCartUseCase removeItemFromCartUseCase,
                        GetCartItemByIdUseCase getCartItemByIdUseCase,
                        GetAllCartItemsUseCase getAllCartItemsUseCase) {
    this.createCartUseCase = createCartUseCase;
    this.addItemToCartUseCase = addItemToCartUseCase;
    this.removeItemFromCartUseCase = removeItemFromCartUseCase;
    this.getCartItemByIdUseCase = getCartItemByIdUseCase;
    this.getAllCartItemsUseCase = getAllCartItemsUseCase;
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

  @GetMapping("/items")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<List<CartItemResponseDTO>> getAllCartItems(@AuthenticationPrincipal Jwt jwt) {
    List<CartItem> items = this.getAllCartItemsUseCase.execute(jwt.getSubject());
    List<CartItemResponseDTO> itemsDTO = items.stream().map(CartItemResponseDTO::new).toList();
    return new ResponsePayload.Builder<List<CartItemResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todos os itens do carrinho")
      .payload(itemsDTO)
      .build();
  }

  @DeleteMapping("/items/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<Void> removeItemFromCart(@PathVariable Long itemId,
                                                  @AuthenticationPrincipal Jwt jwt) {
    CartItem removedItem = this.removeItemFromCartUseCase.execute(itemId, jwt.getSubject());
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Item '" + removedItem.getProductName() + "' removido com sucesso")
      .payload(null)
      .build();
  }

  @GetMapping("/items/{itemId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<CartItemResponseDTO> getCartItemById(@PathVariable Long itemId,
                                                              @AuthenticationPrincipal Jwt jwt) {
    CartItem item = this.getCartItemByIdUseCase.execute(itemId, jwt.getSubject());
    return new ResponsePayload.Builder<CartItemResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Item de id '" + itemId + "' encontrado")
      .payload(new CartItemResponseDTO(item))
      .build();
  }
}
