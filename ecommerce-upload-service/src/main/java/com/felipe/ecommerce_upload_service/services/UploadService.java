package com.felipe.ecommerce_upload_service.services;

import com.felipe.ecommerce_upload_service.config.storage.StorageProperties;
import com.felipe.ecommerce_upload_service.dtos.DeleteImagesDTO;
import com.felipe.ecommerce_upload_service.dtos.ImageDTO;
import com.felipe.ecommerce_upload_service.dtos.ImageResponseDTO;
import com.felipe.ecommerce_upload_service.dtos.ProductUploadDTO;
import com.felipe.ecommerce_upload_service.exceptions.DeleteFailureException;
import com.felipe.ecommerce_upload_service.exceptions.ImageFileNotFoundException;
import com.felipe.ecommerce_upload_service.exceptions.InvalidFileTypeException;
import com.felipe.ecommerce_upload_service.exceptions.UploadFailureException;
import com.felipe.ecommerce_upload_service.persistence.entities.Image;
import com.felipe.ecommerce_upload_service.persistence.entities.ImageFor;
import com.felipe.ecommerce_upload_service.persistence.repositories.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadService {
  private final ImageRepository imageRepository;
  private final Path rootUploadPath;
  private final Logger logger = LoggerFactory.getLogger(UploadService.class);
  private final Map<Integer, String> wrongFiles = new HashMap<>();

  public UploadService(ImageRepository imageRepository, StorageProperties storageProperties) {
    this.imageRepository = imageRepository;
    this.rootUploadPath = Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();
  }

  public List<Image> saveAll(ProductUploadDTO productData, MultipartFile[] images) {
    if(!wrongFiles.isEmpty()) {
      this.logger.debug("=== Cleaning wrong files ===");
      this.wrongFiles.clear();
    }

    if(!isAllFilesValid(images)) {
      this.logger.debug("save() - wrong files quantity: {}", wrongFiles.size());
      throw new InvalidFileTypeException(wrongFiles);
    }

    final List<Image> savedImages = new ArrayList<>(images.length);

    for(int i = 0; i < images.length; i++) {
      final String imageName = generateImageName(productData.productName(), images[i]);
      final String imagePath = "/" + imageName;

      Image imageEntity = Image.builder()
        .name(imageName)
        .path(imagePath)
        .fileType(images[i].getContentType())
        .fileSize(images[i].getSize())
        .originalFileName(images[i].getOriginalFilename())
        .imageFor(i == 0 ? ImageFor.THUMBNAIL : ImageFor.SHOWCASE)
        .productId(productData.productId())
        .build();

      Image savedImage = this.imageRepository.save(imageEntity);
      savedImages.add(savedImage);
      this.logger.info(
        "Image entity successfully saved. Index: {} Name: {} - Path: {} - Of image: {}",
        i, savedImage.getName(), savedImage.getPath(), savedImage.getOriginalFileName()
      );

      try {
        // Write the image file in the upload directory
        Files.copy(images[i].getInputStream(), this.rootUploadPath.resolve(savedImage.getName()));
        this.logger.info("Image file successfully saved. Index: {} - Original name: {}", i, images[i].getOriginalFilename());

      } catch(IOException ex) {
        this.logger.error(
          "Error on save image file. Image original name: {} - Image index: {}", images[i].getOriginalFilename(), i, ex
        );
        throw new UploadFailureException(ex);
      }
    }
    return savedImages;
  }

  public List<ImageResponseDTO> getProductImages(String productIds) {
    final Set<String> productIdsSet = StringUtils.commaDelimitedListToSet(productIds);
    final List<ImageResponseDTO> productImages = new ArrayList<>(productIdsSet.size());

    productIdsSet.forEach(productId -> {
      this.logger.info("Getting images of product with Id: {}", productId);
      final List<ImageDTO> images = this.imageRepository.findAllByProductId(UUID.fromString(productId))
        .stream()
        .map(ImageDTO::new)
        .toList();

      this.logger.info("Found {} images of product with Id \"{}\"", images.size(), productId);
      productImages.add(new ImageResponseDTO(productId, images));
    });
    return productImages;
  }

  public DeleteImagesDTO deleteImages(String productId) {
    final List<Image> foundImages = this.imageRepository.findAllByProductId(UUID.fromString(productId));
    this.logger.info("Found {} images of product with id '{}'", foundImages.size(), productId);
    int deletedImagesCount = 0;

    if(!foundImages.isEmpty()) {
      for (Image image : foundImages) {
        final Path imageToDeletePath = Paths.get(this.rootUploadPath.toString(), image.getPath());

        try {
          final boolean isImageFileDeleted = Files.deleteIfExists(imageToDeletePath);

          if(!isImageFileDeleted) {
            this.logger.error("Error on deleting image. Image file was not found - Path: {}", imageToDeletePath);
            throw new ImageFileNotFoundException("Não foi possível excluir! Imagem '" + image.getName() + "' não encontrada");
          }
          this.logger.info("Deleted image file on path: {}", imageToDeletePath);

        } catch (IOException ex) {
          this.logger.error("Error on deleting image file. Path: {}", imageToDeletePath, ex);
          throw new DeleteFailureException("Ocorreu um erro ao excluir a imagem '" + image.getName() + "'", ex);
        }

        this.imageRepository.delete(image);
        deletedImagesCount++;
        this.logger.info(
          "Image deleted successfully! ImageId: {} - ImageName: {} - DeletedImagesQuantity: {}",
          image.getId(), image.getName(), deletedImagesCount
        );
      }
    }
    return new DeleteImagesDTO(productId, deletedImagesCount);
  }

  private String generateImageName(String productName, MultipartFile file) {
    final String contentType = file.getContentType() != null ? file.getContentType().split("/")[1] : "";
    final long epochMilliseconds = Instant.now().toEpochMilli();
    return String.format("%s-%d.%s", productName, epochMilliseconds, contentType).toLowerCase();
  }

  private boolean isFileTypeValid(MultipartFile file) {
    final String png = MimeTypeUtils.IMAGE_PNG_VALUE;
    final String jpeg = MimeTypeUtils.IMAGE_JPEG_VALUE;
    final String contentType = file.getContentType();
    if(contentType == null) return false;
    return contentType.equals(png) || contentType.equals(jpeg);
  }

  private boolean isAllFilesValid(MultipartFile[] files) {
    int wrongFilesCount = 0;
    this.logger.debug("isAllFilesValid() - wrong files count: {} at start", wrongFilesCount);

    for(int i = 0; i < files.length; i++) {
      if(!isFileTypeValid(files[i])) {
        final String contentType = files[i].getContentType();
        final String fileName = files[i].getOriginalFilename();
        wrongFiles.put(i, String.format("%s-%s", contentType, fileName));
        wrongFilesCount++;
      }
    }
    this.logger.debug("isAllFilesValid() - wrong files count: {} at the end", wrongFilesCount);
    return wrongFilesCount == 0;
  }
}
