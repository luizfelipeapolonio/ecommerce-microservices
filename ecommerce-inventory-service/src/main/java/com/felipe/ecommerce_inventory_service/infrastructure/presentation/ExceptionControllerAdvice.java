package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.BrandAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.InvalidProductQuantityException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ModelAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ProductAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.infrastructure.exceptions.MappingFailureException;
import com.felipe.ecommerce_inventory_service.infrastructure.exceptions.UnprocessableJsonException;
import com.felipe.response.CustomValidationErrors;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler({
    CategoryAlreadyExistsException.class,
    BrandAlreadyExistsException.class,
    ModelAlreadyExistsException.class,
    ProductAlreadyExistsException.class
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
}
