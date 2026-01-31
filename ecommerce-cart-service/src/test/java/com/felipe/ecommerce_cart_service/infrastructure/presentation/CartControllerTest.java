package com.felipe.ecommerce_cart_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_cart_service.core.application.usecases.AddItemToCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.CreateCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.GetAllCartItemsUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.GetCartItemByIdUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.RemoveItemFromCartUseCase;
import com.felipe.ecommerce_cart_service.core.application.usecases.UpdateCartItemUseCase;
import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.AddItemToCartDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CartItemResponseDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CartResponseDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.CreateCartDTO;
import com.felipe.ecommerce_cart_service.infrastructure.dtos.cart.UpdateCartItemDTO;
import com.felipe.ecommerce_cart_service.testutils.DataMock;
import com.felipe.ecommerce_cart_service.testutils.OAuth2TestMockConfiguration;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@Import({OAuth2TestMockConfiguration.class})
public class CartControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private CreateCartUseCase createCartUseCase;

  @MockitoBean
  private AddItemToCartUseCase addItemToCartUseCase;

  @MockitoBean
  private GetAllCartItemsUseCase getAllCartItemsUseCase;

  @MockitoBean
  private RemoveItemFromCartUseCase removeItemFromCartUseCase;

  @MockitoBean
  private GetCartItemByIdUseCase getCartItemByIdUseCase;

  @MockitoBean
  private UpdateCartItemUseCase updateCartItemUseCase;

  private DataMock dataMock;
  private static final String BASE_URL = "/api/v1/carts";
  private static final String CUSTOMER_EMAIL = "test@email.com";

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createCartSuccess - Should return a success response with the created cart")
  void createCartSuccess() throws Exception {
    Cart cart = this.dataMock.getCartsDomain().getFirst();
    CreateCartDTO createCartDTO = new CreateCartDTO(cart.getCustomerId());
    CartResponseDTO responseDTO = new CartResponseDTO(cart);
    String jsonRequestBody = this.objectMapper.writeValueAsString(createCartDTO);

    when(this.createCartUseCase.execute(createCartDTO.customerId())).thenReturn(cart);

    this.mockMvc.perform(post(BASE_URL)
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_admin")))
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isCreated(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.CREATED.value()),
        jsonPath("$.message").value("Carrinho do usuário de id '" + cart.getCustomerId() + "' criado com sucesso"),
        jsonPath("$.payload.id").value(responseDTO.id()),
        jsonPath("$.payload.customerId").value(responseDTO.customerId()),
        jsonPath("$.payload.createdAt").value(responseDTO.createdAt())
      );

    verify(this.createCartUseCase, times(1)).execute(createCartDTO.customerId());
  }

  @Test
  @DisplayName("addItemToCartSuccess - Should return a success response with the added item")
  void addItemToCartSuccess() throws Exception {
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();
    item.setCart(this.dataMock.getCartsDomain().getFirst());
    AddItemToCartDTO itemDTO = new AddItemToCartDTO(item.getProductId(), 1);
    String message = String.format(
      "Item '%s' adicionado com sucesso no carrinho de id '%s'",
      item.getProductName(), item.getCart().getId().toString()
    );
    String jsonRequestBody = this.objectMapper.writeValueAsString(itemDTO);
    CartItemResponseDTO responseDTO = new CartItemResponseDTO(item);

    when(this.addItemToCartUseCase.execute(itemDTO.productId(), itemDTO.quantity(), CUSTOMER_EMAIL)).thenReturn(item);

    this.mockMvc.perform(post(BASE_URL + "/items")
      .with(jwt().jwt(jwt -> jwt.subject(CUSTOMER_EMAIL)))
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isCreated(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.CREATED.value()),
        jsonPath("$.message").value(message),
        jsonPath("$.payload.id").value(responseDTO.id()),
        jsonPath("$.payload.productId").value(responseDTO.productId()),
        jsonPath("$.payload.productName").value(responseDTO.productName()),
        jsonPath("$.payload.thumbnailImage").value(responseDTO.thumbnailImage()),
        jsonPath("$.payload.unitPrice").value(responseDTO.unitPrice()),
        jsonPath("$.payload.quantity").value(responseDTO.quantity()),
        jsonPath("$.payload.discountType").value(responseDTO.discountType()),
        jsonPath("$.payload.discountValue").value(responseDTO.discountValue()),
        jsonPath("$.payload.finalPrice").value(responseDTO.finalPrice()),
        jsonPath("$.payload.addedAt").value(responseDTO.addedAt()),
        jsonPath("$.payload.cartId").value(responseDTO.cartId())
      );

    verify(this.addItemToCartUseCase, times(1)).execute(itemDTO.productId(), itemDTO.quantity(), CUSTOMER_EMAIL);
  }

  @Test
  @DisplayName("getAllCartItemsSuccess - Should return a success response with all cart items")
  void getAllCartItemsSuccess() throws Exception {
    List<CartItem> items = this.dataMock.getCartItemsDomain()
      .stream()
      .map(item -> item.cart(this.dataMock.getCartsDomain().getFirst()))
      .toList();
    List<CartItemResponseDTO> responseDTOs = items.stream().map(CartItemResponseDTO::new).toList();
    var response = new ResponsePayload.Builder<List<CartItemResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todos os itens do carrinho")
      .payload(responseDTOs)
      .build();
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getAllCartItemsUseCase.execute(CUSTOMER_EMAIL)).thenReturn(items);

    this.mockMvc.perform(get(BASE_URL + "/items")
      .with(jwt().jwt(jwt -> jwt.subject(CUSTOMER_EMAIL)))
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getAllCartItemsUseCase, times(1)).execute(CUSTOMER_EMAIL);
  }

  @Test
  @DisplayName("removeItemFromCartSuccess - Should return a success response")
  void removeItemFromCartSuccess() throws Exception {
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();

    when(this.removeItemFromCartUseCase.execute(item.getId(), CUSTOMER_EMAIL)).thenReturn(item);

    this.mockMvc.perform(delete(BASE_URL + "/items/{itemId}", item.getId())
      .with(jwt().jwt(jwt -> jwt.subject(CUSTOMER_EMAIL)))
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Item '" + item.getProductName() + "' removido com sucesso"),
        jsonPath("$.payload").isEmpty()
      );

    verify(this.removeItemFromCartUseCase, times(1)).execute(item.getId(), CUSTOMER_EMAIL);
  }

  @Test
  @DisplayName("getCartItemByIdSuccess - Should return a success response with the found item")
  void getCartItemByIdSuccess() throws Exception {
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();
    item.setCart(this.dataMock.getCartsDomain().getFirst());
    var responseDTO = new CartItemResponseDTO(item);

    when(this.getCartItemByIdUseCase.execute(item.getId(), CUSTOMER_EMAIL)).thenReturn(item);

    this.mockMvc.perform(get(BASE_URL + "/items/{itemId}", item.getId())
      .with(jwt().jwt(jwt -> jwt.subject(CUSTOMER_EMAIL)))
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Item de id '" + item.getId() + "' encontrado"),
        jsonPath("$.payload.id").value(responseDTO.id()),
        jsonPath("$.payload.productId").value(responseDTO.productId()),
        jsonPath("$.payload.productName").value(responseDTO.productName()),
        jsonPath("$.payload.thumbnailImage").value(responseDTO.thumbnailImage()),
        jsonPath("$.payload.unitPrice").value(responseDTO.unitPrice()),
        jsonPath("$.payload.quantity").value(responseDTO.quantity()),
        jsonPath("$.payload.discountType").value(responseDTO.discountType()),
        jsonPath("$.payload.discountValue").value(responseDTO.discountValue()),
        jsonPath("$.payload.finalPrice").value(responseDTO.finalPrice()),
        jsonPath("$.payload.addedAt").value(responseDTO.addedAt()),
        jsonPath("$.payload.cartId").value(responseDTO.cartId())
      );
  }

  @Test
  @DisplayName("updateCartItemSuccess - Should return a success response with the updated cart item")
  void updateCartItemSuccess() throws Exception {
    CartItem item = this.dataMock.getCartItemsDomain().getFirst();
    item.setCart(this.dataMock.getCartsDomain().getFirst());
    var requestDTO = new UpdateCartItemDTO(2);
    var responseDTO = new CartItemResponseDTO(item);
    String jsonRequestBody = this.objectMapper.writeValueAsString(requestDTO);

    when(this.updateCartItemUseCase.execute(item.getId(), requestDTO.quantity(), CUSTOMER_EMAIL)).thenReturn(item);

    this.mockMvc.perform(patch(BASE_URL + "/items/{itemId}", item.getId())
      .with(jwt().jwt(jwt -> jwt.subject(CUSTOMER_EMAIL)))
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.payload.id").value(responseDTO.id()),
        jsonPath("$.payload.productId").value(responseDTO.productId()),
        jsonPath("$.payload.productName").value(responseDTO.productName()),
        jsonPath("$.payload.thumbnailImage").value(responseDTO.thumbnailImage()),
        jsonPath("$.payload.unitPrice").value(responseDTO.unitPrice()),
        jsonPath("$.payload.quantity").value(responseDTO.quantity()),
        jsonPath("$.payload.discountType").value(responseDTO.discountType()),
        jsonPath("$.payload.discountValue").value(responseDTO.discountValue()),
        jsonPath("$.payload.finalPrice").value(responseDTO.finalPrice()),
        jsonPath("$.payload.addedAt").value(responseDTO.addedAt()),
        jsonPath("$.payload.cartId").value(responseDTO.cartId())
      );

    verify(this.updateCartItemUseCase, times(1)).execute(item.getId(), requestDTO.quantity(), CUSTOMER_EMAIL);
  }
}
