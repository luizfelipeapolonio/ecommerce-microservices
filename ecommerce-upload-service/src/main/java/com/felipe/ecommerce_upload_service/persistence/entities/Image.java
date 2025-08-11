package com.felipe.ecommerce_upload_service.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "images")
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String path;

  @Column(name = "file_type", nullable = false, length = 50)
  private String fileType;

  @Column(name = "file_size", nullable = false)
  private Long fileSize;

  @Column(name = "original_file_name", nullable = false, columnDefinition = "TEXT")
  private String originalFileName;

  @Column(name = "image_for", nullable = false, length = 10)
  private String imageFor;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  protected Image() {
  }

  public UUID getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getPath() {
    return this.path;
  }

  public String getFileType() {
    return this.fileType;
  }

  public Long getFileSize() {
    return this.fileSize;
  }

  public String getOriginalFileName() {
    return this.originalFileName;
  }

  public String getImageFor() {
    return this.imageFor;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(Image image) {
    return new Builder(image);
  }

  protected Image(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.path = builder.path;
    this.fileType = builder.fileType;
    this.fileSize = builder.fileSize;
    this.originalFileName = builder.originalFileName;
    this.imageFor = builder.imageFor;
    this.productId = builder.productId;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
  }

  public static class Builder {
    private UUID id;
    private String name;
    private String path;
    private String fileType;
    private Long fileSize;
    private String originalFileName;
    private String imageFor;
    private UUID productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Builder() {
    }

    private Builder(Image image) {
      this.id = image.getId();
      this.name = image.getName();
      this.path = image.getPath();
      this.fileType = image.getFileType();
      this.fileSize = image.getFileSize();
      this.originalFileName = image.getOriginalFileName();
      this.imageFor = image.getImageFor();
      this.productId = image.getProductId();
      this.createdAt = image.getCreatedAt();
      this.updatedAt = image.getUpdatedAt();
    }

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder fileType(String fileType) {
      this.fileType = fileType;
      return this;
    }

    public Builder fileSize(Long fileSize) {
      this.fileSize = fileSize;
      return this;
    }

    public Builder originalFileName(String originalFileName) {
      this.originalFileName = originalFileName;
      return this;
    }

    public Builder imageFor(ImageFor imageFor) {
      this.imageFor = imageFor.text();
      return this;
    }

    public Builder productId(UUID productId) {
      this.productId = productId;
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Image build() {
      return new Image(this);
    }
  }
}