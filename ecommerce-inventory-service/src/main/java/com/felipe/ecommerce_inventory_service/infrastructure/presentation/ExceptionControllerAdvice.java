package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.response.CustomValidationErrors;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler(CategoryAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponsePayload<Void> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex) {
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
}
