package com.felipe.ecommerce_order_service.infrastructure.presentation;

import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.SagaNotFoundException;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
  private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

  @ExceptionHandler(OrderNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponsePayload<Void> handleOrderNotFoundException(OrderNotFoundException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.NOT_FOUND)
      .message(ex.getMessage())
      .build();
  }

  @ExceptionHandler(SagaNotFoundException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleSagaNotFoundException(SagaNotFoundException ex) {
    logger.error("SagaNotFoundException handler -> message: {}", ex.getMessage());
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message("Ocorreu um erro interno do servidor! Por favor, tente mais tarde")
      .build();
  }
}
