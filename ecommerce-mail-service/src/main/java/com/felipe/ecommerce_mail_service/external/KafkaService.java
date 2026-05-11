package com.felipe.ecommerce_mail_service.external;

import com.felipe.ecommerce_mail_service.services.EmailService;
import com.felipe.kafka.EmailDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
  private final EmailService emailService;

  public KafkaService(EmailService emailService) {
    this.emailService = emailService;
  }

  @KafkaListener(topics = "order.emails", groupId = "mail-service")
  void sendEmail(EmailDTO emailDTO) {
    this.emailService.sendEmail(emailDTO);
  }
}
