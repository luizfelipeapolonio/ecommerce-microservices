package com.felipe.ecommerce_upload_service.exceptions;

public class UploadDirectoryInitializationException extends RuntimeException {
  public UploadDirectoryInitializationException(Throwable cause) {
    super("Não foi possível criar o diretório de uploads", cause);
  }
}
