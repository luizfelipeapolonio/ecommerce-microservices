package com.felipe.ecommerce_upload_service.dtos;

import java.util.List;

public record ImageResponseDTO(String productId, List<ImageDTO> images) {
}
