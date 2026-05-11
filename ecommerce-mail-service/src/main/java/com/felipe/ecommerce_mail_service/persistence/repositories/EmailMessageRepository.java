package com.felipe.ecommerce_mail_service.persistence.repositories;

import com.felipe.ecommerce_mail_service.persistence.entities.EmailMessage;
import com.felipe.ecommerce_mail_service.persistence.entities.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long> {

  @Modifying
  @Query("""
    UPDATE EmailMessage em SET em.status = :status, em.sentAt = CURRENT_TIMESTAMP
    WHERE em.mailTo.id = :id AND em.subject = :subject
  """)
  void updateStatusBySubjectAndMailToId(@Param("subject") String subject,
                                        @Param("id") Long mailToId,
                                        @Param("status") EmailStatus status);
}
