package com.felipe.ecommerce_upload_service.exceptions;

import java.util.Map;

public class InvalidFileTypeException extends RuntimeException {
  private final Map<Integer, String> wrongFiles;

  public InvalidFileTypeException(Map<Integer, String> wrongFiles) {
    super("Arquivos inválidos! Por favor, envie apenas PNG ou JPEG");
    this.wrongFiles = wrongFiles;
  }

  public Map<Integer, String> getWrongFilesInfo() {
    return this.wrongFiles;
  }
}
