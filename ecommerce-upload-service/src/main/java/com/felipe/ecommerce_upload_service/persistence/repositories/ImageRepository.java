package com.felipe.ecommerce_upload_service.persistence.repositories;

import com.felipe.ecommerce_upload_service.persistence.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
