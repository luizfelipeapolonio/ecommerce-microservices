package com.felipe.response.product;

import java.util.List;

public record ProductResponseDTO(String id,
                                 String name,
                                 String description,
                                 String unitPrice,
                                 long quantity,
                                 boolean withDiscount,
                                 String discountType,
                                 String discountValue,
                                 String createdAt,
                                 String updatedAt,
                                 CategoryDTO category,
                                 BrandDTO brand,
                                 ModelDTO model,
                                 List<ImageFileDTO> images) {
}
