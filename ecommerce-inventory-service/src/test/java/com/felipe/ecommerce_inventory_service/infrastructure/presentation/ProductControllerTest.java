package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.CreateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.UploadFileImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.CreateProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.ProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.UploadFileMapper;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import com.felipe.ecommerce_inventory_service.testutils.OAuth2TestMockConfiguration;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({OAuth2TestMockConfiguration.class})
public class ProductControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoSpyBean
  ObjectMapper objectMapper;

  @MockitoBean
  CreateProductUseCase createProductUseCase;

  @MockitoBean
  UploadFileMapper uploadFileMapper;

  @MockitoBean
  Validator validator;

  private DataMock dataMock;
  private static final String BASE_URL = "/api/v1/products";

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createProductSuccess - Should return a success response with the created product")
  void createProductSuccess() throws Exception {
    Product product = this.dataMock.getProductsDomain().getFirst();

    // Request mocks
    CreateProductDTO productDTO = new CreateProductDTO(
      product.getName(),
      product.getDescription(),
      product.getUnitPrice().toString(),
      product.getQuantity(),
      product.getCategory().getId(),
      product.getBrand().getId(),
      product.getModel().getId()
    );
    String jsonRequestBody = this.objectMapper.writeValueAsString(productDTO);

    MultipartFile[] mockFilesArray = {
      new MockMultipartFile(
      "image1",
      "image1",
      "image/png",
      new byte[]{10, 20, 30, 40})
    };
    UploadFile uploadFile = UploadFileImpl.builder()
      .originalFileName(mockFilesArray[0].getOriginalFilename())
      .contentType(mockFilesArray[0].getContentType())
      .content(mockFilesArray[0].getBytes())
      .size(mockFilesArray[0].getSize())
      .inputStream(mockFilesArray[0].getBytes())
      .build();

    // Response mocks
    ImageFileDTO image1 = new ImageFileDTO(
      "01",
      "image1",
      "imagePath",
      "image/png",
      "123456",
      "image1",
      "thumbnail",
      "01",
      "anything",
      "anything"
    );
    ProductDTO productResponseDTO = new ProductDTO(product, List.of(image1));

    ResponsePayload<CreateProductResponseDTO> response = new ResponsePayload.Builder<CreateProductResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Produto '" + productResponseDTO.name() + "' inserido com sucesso")
      .payload(productResponseDTO)
      .build();
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.objectMapper.readValue(jsonRequestBody, CreateProductDTO.class)).thenReturn(productDTO);
    when(this.validator.validate(productDTO)).thenReturn(Set.of());
    when(this.uploadFileMapper.toUploadFile(any(MultipartFile.class))).thenReturn(uploadFile);
    when(this.createProductUseCase.execute(eq(productDTO), any(UploadFile[].class))).thenReturn(productResponseDTO);

    this.mockMvc.perform(multipart(HttpMethod.POST, BASE_URL)
      .file("productDTO", jsonRequestBody.getBytes())
      .file("images", mockFilesArray[0].getBytes())
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isCreated(), content().json(jsonResponseBody));

    verify(this.uploadFileMapper, times(1)).toUploadFile(any(MultipartFile.class));
    verify(this.createProductUseCase, times(1)).execute(eq(productDTO), any(UploadFile[].class));
    verify(this.objectMapper, times(2)).readValue(jsonRequestBody, CreateProductDTO.class);
    verify(this.validator, times(1)).validate(productDTO);
  }
}
