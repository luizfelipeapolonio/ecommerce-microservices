package com.felipe.ecommerce_payment_service.infrastructure.dtos;

public record AddressDTO(String street,
                  String number,
                  String complement,
                  String district,
                  String zipcode,
                  String city,
                  String state,
                  String country) {}
