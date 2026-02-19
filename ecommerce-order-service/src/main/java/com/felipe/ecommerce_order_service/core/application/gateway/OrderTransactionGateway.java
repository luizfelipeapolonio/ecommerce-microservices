package com.felipe.ecommerce_order_service.core.application.gateway;

import java.util.UUID;

public interface OrderTransactionGateway {
  UUID executeOrderTransaction(UUID orderId, UUID productId, int productQuantity);
}
