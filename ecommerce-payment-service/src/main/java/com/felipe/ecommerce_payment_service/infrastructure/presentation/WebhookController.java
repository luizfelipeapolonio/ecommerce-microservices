package com.felipe.ecommerce_payment_service.infrastructure.presentation;

import com.felipe.ecommerce_payment_service.infrastructure.external.EventHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/stripe")
public class WebhookController {
  private final EventHandler eventHandler;

  public WebhookController(EventHandler eventHandler) {
    this.eventHandler = eventHandler;
  }

  @PostMapping
  public void handleEvents(@RequestBody String payload, @RequestHeader("Stripe-Signature") String stripeHeader) {
    this.eventHandler.handle(payload, stripeHeader);
  }
}
