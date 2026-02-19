package com.felipe.ecommerce_inventory_service.infrastructure.config.beans;

import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.DeleteReservationUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.ReserveProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl.DeleteReservationUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl.ReserveProductUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ReservationBeans {
  private final ReservationGateway reservationGateway;
  private final ProductGateway productGateway;

  public ReservationBeans(ReservationGateway reservationGateway, ProductGateway productGateway) {
    this.reservationGateway = reservationGateway;
    this.productGateway = productGateway;
  }

  @Bean
  public ReserveProductUseCase reserveProductUseCase() {
    return new ReserveProductUseCaseImpl(this.reservationGateway, this.productGateway);
  }

  @Bean
  public DeleteReservationUseCase deleteReservationUseCase() {
    return new DeleteReservationUseCaseImpl(this.reservationGateway);
  }
}
