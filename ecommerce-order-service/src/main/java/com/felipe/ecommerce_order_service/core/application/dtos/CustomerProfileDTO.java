package com.felipe.ecommerce_order_service.core.application.dtos;

public record CustomerProfileDTO(String id,
                                 String email,
                                 String username,
                                 String firstName,
                                 String lastName,
                                 String createdAt,
                                 String updatedAt,
                                 AddressDTO address) {
}
