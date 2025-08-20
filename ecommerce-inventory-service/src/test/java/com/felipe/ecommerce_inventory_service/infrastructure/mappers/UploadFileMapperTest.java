package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.UploadFileImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UploadFileMapperTest {

  @Spy
  private UploadFileMapper uploadFileMapper;
  private UploadFile uploadFile;
  private MultipartFile multipartFile;

  @BeforeEach
  void setUp() {
    byte[] content = {10, 20, 30, 40};

    this.uploadFile = UploadFileImpl.builder()
      .originalFileName("image1")
      .content(content)
      .contentType("image/png")
      .inputStream(content)
      .size(123456)
      .build();

    this.multipartFile = new MockMultipartFile(
      "image1",
      "image1",
      "image/png",
      content
    );
  }

  @Test
  @DisplayName("convertMultipartFileToUploadFile - Should successfully convert a MultipartFile to UploadFile")
  void convertMultipartFileToUploadFile() throws IOException {
    MultipartFile convertedFile = this.uploadFileMapper.toMultipartFile(this.uploadFile);

    assertThat(convertedFile.getOriginalFilename()).isEqualTo(this.uploadFile.getOriginalName());
    assertThat(convertedFile.getBytes()).isEqualTo(this.uploadFile.getContent());
    assertThat(convertedFile.getContentType()).isEqualTo(this.uploadFile.getContentType());
    assertThat(convertedFile.getSize()).isEqualTo(this.uploadFile.getSize());
    assertThat(convertedFile.getInputStream()).isNotEmpty();
  }

  @Test
  @DisplayName("convertUploadFileToMultipartFile - Should convert an UploadFile to MultipartFile")
  void convertUploadFileToMultipartFile() throws IOException {
    UploadFile convertedFile = this.uploadFileMapper.toUploadFile(this.multipartFile);

    assertThat(convertedFile.getOriginalName()).isEqualTo(this.multipartFile.getOriginalFilename());
    assertThat(convertedFile.getContent()).isEqualTo(this.multipartFile.getBytes());
    assertThat(convertedFile.getContentType()).isEqualTo(this.multipartFile.getContentType());
    assertThat(convertedFile.getSize()).isEqualTo(this.multipartFile.getSize());
    assertThat(convertedFile.getInputStream()).isNotEmpty();
  }
}
