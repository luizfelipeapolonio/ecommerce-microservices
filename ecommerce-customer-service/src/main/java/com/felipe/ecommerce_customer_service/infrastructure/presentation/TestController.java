package com.felipe.ecommerce_customer_service.infrastructure.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class TestController {

  @GetMapping("/test")
  public String test() {
    return "You accessed a protected resource";
  }

  @GetMapping("/admin")
  public String admin() {
    return "You accessed an ADMIN resource";
  }
}
