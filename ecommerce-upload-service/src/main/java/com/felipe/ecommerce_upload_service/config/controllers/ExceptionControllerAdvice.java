package com.felipe.ecommerce_upload_service.config.controllers;

import com.felipe.ecommerce_upload_service.dtos.WrongFileDTO;
import com.felipe.ecommerce_upload_service.exceptions.InvalidFileTypeException;
import com.felipe.ecommerce_upload_service.exceptions.UnprocessableJsonException;
import com.felipe.ecommerce_upload_service.exceptions.UploadDirectoryInitializationException;
import com.felipe.ecommerce_upload_service.exceptions.UploadFailureException;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler({UploadDirectoryInitializationException.class, UploadFailureException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponsePayload<Void> handleUploadExceptions(Exception ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.INTERNAL_SERVER_ERROR)
      .message(ex.getMessage())
      .payload(null)
      .build();
  }

  @ExceptionHandler(InvalidFileTypeException.class)
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public ResponsePayload<List<WrongFileDTO>> handleInvalidFileTypeException(InvalidFileTypeException ex) {
    final Set<Map.Entry<Integer, String>> wrongFilesInfo = ex.getWrongFilesInfo().entrySet();
    final List<WrongFileDTO> wrongFilesDTO = new ArrayList<>();

    for(Map.Entry<Integer, String> fileInfo : wrongFilesInfo) {
      final String[] value = fileInfo.getValue().split("-");
      final String contentType = value[0];
      final String fileName = value[1];
      wrongFilesDTO.add(new WrongFileDTO(fileInfo.getKey(), contentType, fileName));
    }

    return new ResponsePayload.Builder<List<WrongFileDTO>>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
      .message(ex.getMessage())
      .payload(wrongFilesDTO)
      .build();
  }

  @ExceptionHandler(UnprocessableJsonException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponsePayload<Void> handleUnprocessableJsonException(UnprocessableJsonException ex) {
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.ERROR)
      .code(HttpStatus.UNPROCESSABLE_ENTITY)
      .message(ex.getMessage())
      .payload(null)
      .build();
  }
}
