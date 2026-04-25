package com.felipe.ecommerce_order_service.core.application.dtos;

import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class UpdateOrderDTO {
  private OrderStatus status;
  private String checkoutUrl;
  private String invoiceUrl;
  private String orderPrice;
  private Boolean withCoupon;
  private final List<UpdateProductDTO> products = new ArrayList<>();

  public UpdateOrderDTO() {
  }

  public UpdateOrderDTO updateStatus(OrderStatus status) {
    this.status = status;
    return this;
  }

  public OrderStatus status() {
    return this.status;
  }

  public UpdateOrderDTO updateCheckoutUrl(String checkoutUrl) {
    this.checkoutUrl = checkoutUrl;
    return this;
  }

  public String checkoutUrl() {
    return this.checkoutUrl;
  }

  public UpdateOrderDTO updateInvoiceUrl(String invoiceUrl) {
    this.invoiceUrl = invoiceUrl;
    return this;
  }

  public String invoiceUrl() {
    return this.invoiceUrl;
  }

  public UpdateOrderDTO updateOrderPrice(String orderPrice) {
    this.orderPrice = orderPrice;
    return this;
  }

  public String orderPrice() {
    return this.orderPrice;
  }

  public UpdateOrderDTO updateWithCoupon(boolean withCoupon) {
    this.withCoupon = withCoupon;
    return this;
  }

  public Boolean withCoupon() {
    return this.withCoupon;
  }

  public UpdateOrderDTO updateProduct(UpdateProductDTO product) {
    this.products.add(product);
    return this;
  }

  public UpdateOrderDTO updateProducts(List<UpdateProductDTO> products) {
    this.products.addAll(products);
    return this;
  }

  public List<UpdateProductDTO> products() {
    return this.products;
  }
}
