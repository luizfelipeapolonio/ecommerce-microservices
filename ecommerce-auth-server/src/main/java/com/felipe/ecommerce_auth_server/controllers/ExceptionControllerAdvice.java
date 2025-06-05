package com.felipe.ecommerce_auth_server.controllers;

import com.felipe.ecommerce_auth_server.dtos.ResponseDTO;
import com.felipe.ecommerce_auth_server.enums.StatusResponse;
import com.felipe.ecommerce_auth_server.exceptions.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler(EmailAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseDTO handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
    return new ResponseDTO(StatusResponse.ERROR, HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseDTO handleUncaughtException() {
    return new ResponseDTO(
      StatusResponse.ERROR,
      HttpStatus.INTERNAL_SERVER_ERROR,
      "Ocorreu um erro interno do servidor"
    );
  }
}
