package com.felipe.ecommerce_upload_service.exceptions;

public class UploadFailureException extends RuntimeException {
  public UploadFailureException(Throwable cause) {
    super("Ocorreu um erro! Não foi possível concluir o upload", cause);
  }
}
