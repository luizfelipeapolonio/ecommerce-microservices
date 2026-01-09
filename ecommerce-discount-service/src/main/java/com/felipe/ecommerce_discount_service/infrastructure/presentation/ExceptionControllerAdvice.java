package com.felipe.ecommerce_discount_service.infrastructure.presentation;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidDiscountTypeException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidEndDateException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidPromotionAppliesTargetException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidPromotionScopeException;
import com.felipe.ecommerce_discount_service.infrastructure.exceptions.CreatePromotionDTOValidationException;
import com.felipe.ecommerce_discount_service.infrastructure.exceptions.InventoryServiceException;
import com.felipe.response.CustomValidationErrors;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
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

  @ExceptionHandler(DataNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponsePayload<Void> handleDataNotFoundException(DataNotFoundException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.NOT_FOUND)
      .message(ex.getMessage())
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

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponsePayload<List<CustomValidationErrors>> handleConstraintViolationException(ConstraintViolationException ex) {
    List<CustomValidationErrors> errors = ex.getConstraintViolations()
      .stream()
      .map(constraintViolation -> {
        String field = constraintViolation.getPropertyPath().toString();
        field = field.contains(".") ? field.split("\\.")[1] : field;
        CustomValidationErrors validationError = new CustomValidationErrors();
        validationError.setField(field);
        validationError.setRejectedValue(constraintViolation.getInvalidValue());
        validationError.setCause(constraintViolation.getMessage());
        return validationError;
      })
      .toList();

    return new ResponsePayload.Builder<List<CustomValidationErrors>>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.BAD_REQUEST)
      .message("Erros de validação")
      .payload(errors)
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

  @ExceptionHandler(RequestNotPermitted.class)
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  public ResponsePayload<Void> handleRequestNotPermittedException() {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.TOO_MANY_REQUESTS)
      .message("Muitas requisições foram feitas em um determinado período de tempo. Por favor, tente novamente mais tarde")
      .build();
  }

  @ExceptionHandler(CallNotPermittedException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleCallNotPermittedException() {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message("Ocorreu um erro ao se comunicar com o servidor")
      .build();
  }

  @ExceptionHandler(InventoryServiceException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleInventoryServiceException(InventoryServiceException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message(ex.getMessage())
      .build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleUncaughtException(Exception ex) {
    this.logger.error("Uncaught exception handler: {}", ex.getMessage());
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message("Ocorreu um erro interno do servidor! Por favor, tente mais tarde")
      .build();
  }
}
