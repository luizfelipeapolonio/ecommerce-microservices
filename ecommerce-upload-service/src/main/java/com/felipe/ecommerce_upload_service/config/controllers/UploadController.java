package com.felipe.ecommerce_upload_service.config.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_upload_service.dtos.ImageDTO;
import com.felipe.ecommerce_upload_service.dtos.ProductUploadDTO;
import com.felipe.ecommerce_upload_service.exceptions.UnprocessableJsonException;
import com.felipe.ecommerce_upload_service.services.UploadService;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/internal/uploads")
public class UploadController {
  private final UploadService uploadService;
  private final ObjectMapper objectMapper;
  private final Logger logger = LoggerFactory.getLogger(UploadController.class);

  public UploadController(UploadService uploadService, ObjectMapper objectMapper) {
    this.uploadService = uploadService;
    this.objectMapper = objectMapper;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<List<ImageDTO>> save(@RequestPart("productData") String jsonProductData,
                                              @RequestPart("images") MultipartFile[] images) {
    try {
      ProductUploadDTO productDTO = this.objectMapper.readValue(jsonProductData, ProductUploadDTO.class);
      List<ImageDTO> savedImages = this.uploadService.save(productDTO, images)
        .stream()
        .map(ImageDTO::new)
        .toList();

      return new ResponsePayload.Builder<List<ImageDTO>>()
        .type(ResponseType.SUCCESS)
        .code(HttpStatus.CREATED)
        .message("Imagens salvas com sucesso")
        .payload(savedImages)
        .build();

    } catch(JsonProcessingException ex) {
      this.logger.error("Error on convert JSON 'productData' to ProductUploadDTO: {}", ex.getMessage(), ex);
      throw new UnprocessableJsonException("Não foi possível converter JSON para objeto", ex);
    }
  }
}
