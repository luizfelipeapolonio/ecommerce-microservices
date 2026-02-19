package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.BrandAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.InvalidProductQuantityException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ModelAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ProductAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ReservationAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.UnavailableProductException;
import com.felipe.ecommerce_inventory_service.infrastructure.exceptions.MappingFailureException;
import com.felipe.ecommerce_inventory_service.infrastructure.exceptions.UnprocessableJsonException;
import com.felipe.ecommerce_inventory_service.infrastructure.exceptions.UploadServiceException;
import com.felipe.response.CustomValidationErrors;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionControllerAdvice {
  private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

  @ExceptionHandler({
    CategoryAlreadyExistsException.class,
    BrandAlreadyExistsException.class,
    ModelAlreadyExistsException.class,
    ProductAlreadyExistsException.class,
    ReservationAlreadyExistsException.class
  })
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponsePayload<Void> handleResourceAlreadyExistsException(Exception ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.CONFLICT)
      .message(ex.getMessage())
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponsePayload<List<CustomValidationErrors>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<CustomValidationErrors> errors = ex.getBindingResult()
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

  @ExceptionHandler(UnprocessableJsonException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponsePayload<Void> handleUnprocessableJsonException(UnprocessableJsonException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.UNPROCESSABLE_ENTITY)
      .message(ex.getMessage())
      .build();
  }

  @ExceptionHandler(UnavailableProductException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponsePayload<Void> handleUnavailableProductException(UnavailableProductException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.UNPROCESSABLE_ENTITY)
      .message(ex.getMessage())
      .build();
  }

  @ExceptionHandler(InvalidProductQuantityException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponsePayload<Void> handleInvalidProductQuantityException(InvalidProductQuantityException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.BAD_REQUEST)
      .message(ex.getMessage())
      .build();
  }

  @ExceptionHandler(MappingFailureException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleMappingFailureException(MappingFailureException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message(ex.getMessage())
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

  @ExceptionHandler(UploadServiceException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleUploadServiceException(UploadServiceException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message(ex.getMessage())
      .build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleUncaughtException(Exception ex) {
    logger.error("Uncaught exception handler: {}", ex.getMessage());
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message("Ocorreu um erro interno do servidor! Por favor, tente mais tarde")
      .build();
  }
}
