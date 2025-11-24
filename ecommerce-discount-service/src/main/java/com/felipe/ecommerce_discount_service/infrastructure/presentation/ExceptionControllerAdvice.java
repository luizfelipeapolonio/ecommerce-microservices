package com.felipe.ecommerce_discount_service.infrastructure.presentation;

import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidDiscountTypeException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidEndDateException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidPromotionAppliesTargetException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidPromotionScopeException;
import com.felipe.ecommerce_discount_service.infrastructure.exceptions.CreatePromotionDTOValidationException;
import com.felipe.response.CustomValidationErrors;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionControllerAdvice {
  private final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponsePayload<List<CustomValidationErrors>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    final List<CustomValidationErrors> errors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(CustomValidationErrors::new)
      .toList();

    return new ResponsePayload.Builder<List<CustomValidationErrors>>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.UNPROCESSABLE_ENTITY)
      .message("Erros de validação")
      .payload(errors)
      .build();
  }

  @ExceptionHandler(CreatePromotionDTOValidationException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponsePayload<List<CustomValidationErrors>> handleCreatePromotionDTOValidationException(CreatePromotionDTOValidationException ex) {
    final CustomValidationErrors error = new CustomValidationErrors();
    error.setField(ex.getField());
    error.setRejectedValue(ex.getRejectedValue());
    error.setCause(ex.getMessage());

    return new ResponsePayload.Builder<List<CustomValidationErrors>>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.UNPROCESSABLE_ENTITY)
      .message("Erros de validação")
      .payload(List.of(error))
      .build();
  }

  @ExceptionHandler({
    InvalidDiscountTypeException.class,
    InvalidEndDateException.class,
    InvalidPromotionAppliesTargetException.class,
    InvalidPromotionScopeException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponsePayload<Void> handleCreatePromotionExceptions(Exception ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.BAD_REQUEST)
      .message(ex.getMessage())
      .build();
  }

  @ExceptionHandler(KafkaProducerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleKafkaProducerException(KafkaProducerException ex) {
    this.logger.error(
      "Message: {} \nFailed on topic: {}\nWith the value: {}",
      ex.getMessage(),
      ex.getFailedProducerRecord().topic(),
      ex.getFailedProducerRecord().value()
    );

    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message("Ocorreu um erro interno do servidor. Por favor, tente mais tarde")
      .build();
  }
}
