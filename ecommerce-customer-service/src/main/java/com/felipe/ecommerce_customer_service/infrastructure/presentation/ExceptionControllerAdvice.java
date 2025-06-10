package com.felipe.ecommerce_customer_service.infrastructure.presentation;

import com.felipe.ecommerce_customer_service.core.application.exceptions.AuthServerException;
import com.felipe.ecommerce_customer_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_customer_service.core.application.exceptions.EmailAlreadyExistsException;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler(EmailAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponsePayload<Void> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
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

  @ExceptionHandler(AuthServerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleAuthServerException(AuthServerException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message(ex.getMessage())
      .build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleUncaughtException() {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message("Ocorreu um erro interno do servidor. Por favor, tente novamente mais tarde!")
      .build();
  }
}
