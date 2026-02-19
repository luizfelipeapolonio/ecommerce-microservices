package com.felipe.ecommerce_order_service.core.application.dtos;

record AddressDTO(String street,
                  String number,
                  String complement,
                  String district,
                  String zipcode,
                  String city,
                  String state,
                  String country) {
}
