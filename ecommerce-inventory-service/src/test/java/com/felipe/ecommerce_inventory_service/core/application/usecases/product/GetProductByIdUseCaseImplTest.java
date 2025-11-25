package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetProductByIdUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class GetProductByIdUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  private GetProductByIdUseCaseImpl getProductByIdUseCase;
  private List<Product> productsDomain;

  @BeforeEach
  void setUp() {
    this.getProductByIdUseCase = new GetProductByIdUseCaseImpl(this.productGateway);
    this.productsDomain = new DataMock().getProductsDomain();
  }

  @Test
  @DisplayName("getProductByIdSuccess - Should successfully find and return a product")
  void getProductByIdSuccess() {
    final Product product = this.productsDomain.getFirst();
    final ProductResponseDTO productResponse = new ProductDTO(product, List.of());

    when(this.productGateway.findProductById(product.getId())).thenReturn(Optional.of(product));
    when(this.productGateway.getProduct(product)).thenReturn(productResponse);

    ProductResponseDTO foundProduct = this.getProductByIdUseCase.execute(product.getId());

    assertThat(foundProduct.id()).isEqualTo(product.getId().toString());
    assertThat(foundProduct.name()).isEqualTo(product.getName());
    assertThat(foundProduct.description()).isEqualTo(product.getDescription());
    assertThat(foundProduct.quantity()).isEqualTo(product.getQuantity());
    assertThat(foundProduct.unitPrice()).isEqualTo(product.getUnitPrice().toString());
    assertThat(foundProduct.createdAt()).isEqualTo(product.getCreatedAt().toString());
    assertThat(foundProduct.updatedAt()).isEqualTo(product.getUpdatedAt().toString());

    verify(this.productGateway, times(1)).findProductById(product.getId());
    verify(this.productGateway, times(1)).getProduct(product);
  }

  @Test
  @DisplayName("getProductByIdFailsByProductNotFound - Should throw a DataNotFoundException if the product is not found")
  void getProductByIdFailsByProductNotFound() {
    final UUID productId = this.productsDomain.getFirst().getId();

    when(this.productGateway.findProductById(productId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getProductByIdUseCase.execute(productId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Produto de id: '%s' não encontrado", productId);

    verify(this.productGateway, times(1)).findProductById(productId);
    verify(this.productGateway, never()).getProduct(any(Product.class));
  }

  private record ProductDTO(String id,
                            String name,
                            String description,
                            String unitPrice,
                            long quantity,
                            boolean withDiscount,
                            String discountType,
                            String discountValue,
                            String createdAt,
                            String updatedAt,
                            CategoryDTO category,
                            BrandDTO brand,
                            ModelDTO model,
                            List<ImageFileDTO> images) implements ProductResponseDTO {
    public ProductDTO(Product product, List<ImageFileDTO> images) {
      this(
        product.getId().toString(),
        product.getName(),
        product.getDescription(),
        product.getUnitPrice().toString(),
        product.getQuantity(),
        product.isItWithDiscount(),
        product.getDiscountType() == null ? null : product.getDiscountType(),
        product.getDiscountValue() == null ? null : product.getDiscountValue(),
        product.getCreatedAt().toString(),
        product.getUpdatedAt().toString(),
        new CategoryDTO(product.getCategory()),
        new BrandDTO(product.getBrand()),
        new ModelDTO(product.getModel()),
        images
      );
    }
  }
}
