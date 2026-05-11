package com.felipe.ecommerce_mail_service.services;

import com.felipe.ecommerce_mail_service.exceptions.MailToNotFoundException;
import com.felipe.ecommerce_mail_service.external.CustomerService;
import com.felipe.ecommerce_mail_service.external.OrderService;
import com.felipe.ecommerce_mail_service.persistence.entities.EmailMessage;
import com.felipe.ecommerce_mail_service.persistence.entities.EmailStatus;
import com.felipe.ecommerce_mail_service.persistence.entities.MailTo;
import com.felipe.ecommerce_mail_service.persistence.entities.OrderItem;
import com.felipe.ecommerce_mail_service.persistence.repositories.EmailMessageRepository;
import com.felipe.ecommerce_mail_service.persistence.repositories.MailToRepository;
import com.felipe.kafka.EmailDTO;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.UUID;
import java.util.stream.Stream;

@Service
public class EmailService {

  @Value("${spring.mail.from}")
  private String emailFrom;
  private final JavaMailSender javaMailSender;
  private final SpringTemplateEngine templateEngine;
  private final CustomerService customerService;
  private final OrderService orderService;
  private final MailToRepository mailToRepository;
  private final EmailMessageRepository emailMessageRepository;
  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  public EmailService(JavaMailSender javaMailSender,
                      SpringTemplateEngine templateEngine,
                      CustomerService customerService,
                      OrderService orderService,
                      MailToRepository mailToRepository,
                      EmailMessageRepository emailMessageRepository) {
    this.javaMailSender = javaMailSender;
    this.templateEngine = templateEngine;
    this.customerService = customerService;
    this.orderService = orderService;
    this.mailToRepository = mailToRepository;
    this.emailMessageRepository = emailMessageRepository;
  }

  @Transactional
  public void sendEmail(EmailDTO emailDTO) {
    try {
      MailTo mailTo = performActionBySubject(emailDTO);

      Context thymeleafContext = new Context();
      thymeleafContext.setVariable("mailTo", mailTo);
      thymeleafContext.setVariable("subject", emailDTO.getSubject());

      String htmlContent = this.templateEngine.process("email-template.html", thymeleafContext);

      MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      helper.setFrom(this.emailFrom);
      helper.setSubject(emailDTO.getSubject().text());
      helper.setTo(mailTo.getEmail());
      helper.setText(htmlContent, true);

      this.javaMailSender.send(mimeMessage);
      updateEmailMessageToSent(emailDTO.getSubject(), mailTo.getId());
    } catch (Exception ex) {
      logger.error("Error in sending email -> {}", ex.getMessage(), ex);
    }
  }

  private MailTo performActionBySubject(EmailDTO emailDTO) {
    return switch (emailDTO.getSubject()) {
      case APPROVED_PAYMENT -> initMailToInfo(emailDTO);
      case CREATED_INVOICE -> addInvoiceUrl(emailDTO);
      case PREPARING_SHIPMENT -> addTrackingCode(emailDTO);
      case SHIPMENT_OUT_FOR_DELIVERY, DELIVERED_SHIPMENT -> getMailToByOrderId(emailDTO.getOrderId());
    };
  }

  private MailTo initMailToInfo(EmailDTO emailDTO) {
    // Fetching customer profile and order info
    var customerProfile = this.customerService.fetchAuthCustomerProfile(emailDTO.getEmailTo());
    var order = this.orderService.fetchOrderInfo(emailDTO.getOrderId());

    MailTo mailTo = new MailTo();
    mailTo.setEmail(emailDTO.getEmailTo());
    mailTo.setUsername(customerProfile.username());
    mailTo.setOrderId(emailDTO.getOrderId());
    mailTo.setOrderPrice(order.orderPrice());
    mailTo.setShippingFee(order.shippingFee());
    mailTo.setCouponValue(order.couponValue());

    order.orderItems().forEach(orderItem -> {
      OrderItem item = new OrderItem();
      item.setName(orderItem.productName());
      item.setQuantity(orderItem.quantity());
      item.setPrice(orderItem.finalPrice());

      mailTo.addOrderItem(item);
    });
    Stream.of(EmailDTO.Subject.values()).forEach(subject -> {
      EmailMessage message = new EmailMessage();
      message.setSubject(subject.name());

      mailTo.addEmailMessage(message);
    });

    return this.mailToRepository.save(mailTo);
  }

  private MailTo addInvoiceUrl(EmailDTO emailDTO) {
    MailTo mailTo = getMailToByOrderId(emailDTO.getOrderId());
    mailTo.setInvoiceUrl(emailDTO.getInvoiceUrl());
    return this.mailToRepository.save(mailTo);
  }

  private MailTo addTrackingCode(EmailDTO emailDTO) {
    MailTo mailTo = getMailToByOrderId(emailDTO.getOrderId());
    mailTo.setTrackingCode(emailDTO.getTrackingCode());
    return this.mailToRepository.save(mailTo);
  }

  private void updateEmailMessageToSent(EmailDTO.Subject subject, Long mailToId) {
    this.emailMessageRepository.updateStatusBySubjectAndMailToId(subject.name(), mailToId, EmailStatus.SENT);
  }

  private MailTo getMailToByOrderId(UUID orderId) {
    return this.mailToRepository.findByOrderId(orderId)
      .orElseThrow(() -> new MailToNotFoundException("Envio de email para pedido de id '" + orderId + "' não encontrado"));
  }
}
