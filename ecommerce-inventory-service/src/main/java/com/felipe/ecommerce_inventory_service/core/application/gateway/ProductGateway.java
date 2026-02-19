package com.felipe.ecommerce_inventory_service.core.application.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PromotionDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductGateway {
  ProductResponseDTO createProduct(Product product, UploadFile[] files);
  Optional<Product> findProductByName(String name);
  Optional<Product> findProductById(UUID id);
  Optional<Product> findProductByIdWithTransactionLock(UUID id);
  Product updateProduct(Product product, UpdateProductDomainDTO productDTO);
  ProductResponseDTO getProduct(Product product);
  PageResponseDTO getProducts(String category, String brand, String model, int page, int elementsQuantity);
  PageResponseDTO getAllProducts(int page, int elementsQuantity);
  PageResponseDTO getProductsByCategory(String categoryName, int page, int elementsQuantity);
  PageResponseDTO getProductsByBrand(String brandName, int page, int elementsQuantity);
  PageResponseDTO getProductsByModel(String modelName, String brandName, int page, int elementsQuantity);
  Product deleteProduct(Product product);
  long updateProductQuantityInStock(Product product);
  int applyPromotionToProducts(PromotionDTO promotionDTO);
}
