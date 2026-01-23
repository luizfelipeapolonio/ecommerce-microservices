package com.felipe.ecommerce_cart_service.core.application.dtos;

public record CustomerProfileDTO(String id,
                                 String email,
                                 String username,
                                 String firstName,
                                 String lastName,
                                 String createdAt,
                                 String updatedAt,
                                 AddressDTO address) {
}
