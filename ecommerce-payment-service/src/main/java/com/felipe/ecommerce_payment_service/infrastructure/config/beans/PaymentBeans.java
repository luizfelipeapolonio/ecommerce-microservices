package com.felipe.ecommerce_payment_service.infrastructure.config.beans;

import com.felipe.ecommerce_payment_service.core.application.gateway.PaymentGateway;
import com.felipe.ecommerce_payment_service.core.application.usecases.CreatePaymentUseCase;
import com.felipe.ecommerce_payment_service.core.application.usecases.GetPaymentByOrderIdUseCase;
import com.felipe.ecommerce_payment_service.core.application.usecases.UpdatePaymentUseCase;
import com.felipe.ecommerce_payment_service.core.application.usecases.impl.CreatePaymentUseCaseImpl;
import com.felipe.ecommerce_payment_service.core.application.usecases.impl.GetPaymentByOrderIdUseCaseImpl;
import com.felipe.ecommerce_payment_service.core.application.usecases.impl.UpdatePaymentUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentBeans {
  private final PaymentGateway paymentGateway;

  public PaymentBeans(PaymentGateway paymentGateway) {
    this.paymentGateway = paymentGateway;
  }

  @Bean
  public CreatePaymentUseCase createPaymentUseCase() {
    return new CreatePaymentUseCaseImpl(this.paymentGateway);
  }

  @Bean
  public GetPaymentByOrderIdUseCase getPaymentByOrderIdUseCase() {
    return new GetPaymentByOrderIdUseCaseImpl(this.paymentGateway);
  }

  @Bean
  public UpdatePaymentUseCase updatePaymentUseCase() {
    return new UpdatePaymentUseCaseImpl(this.paymentGateway);
  }
}
