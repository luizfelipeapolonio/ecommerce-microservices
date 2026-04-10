package com.felipe.ecommerce_order_service.infrastructure.saga.utils;

import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryTransitionDataHolder {
  private List<ProductData> products;
  private static final Logger logger = LoggerFactory.getLogger(InventoryTransitionDataHolder.class);

  public List<ProductData> getProducts() {
    return this.products;
  }

  public void setProducts(List<InventoryTransactionReply.ProductData> replyProducts) {
    this.products = replyProducts.stream().map(ProductData::new).toList();
    logger.info("Setting products in the InventoryTransitionDataHolder");
  }

  public record ProductData(String name, long quantity, String unitPrice, String discountType, String discountValue) {
    public ProductData(InventoryTransactionReply.ProductData replyProduct) {
      this(
        replyProduct.getName(),
        replyProduct.getQuantity(),
        replyProduct.getUnitPrice(),
        replyProduct.getDiscountType(),
        replyProduct.getDiscountValue()
      );
    }
  }
}
