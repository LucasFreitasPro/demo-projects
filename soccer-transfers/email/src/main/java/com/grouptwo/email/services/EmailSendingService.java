package com.grouptwo.email.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.grouptwo.soccer.transfers.lib.requests.EmailSendingRequest;

@Service
public class EmailSendingService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private JavaMailSender emailSender;

	@Value(value = "${spring.mail.username}")
	private String fromEmail;

	public EmailSendingService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	public ResponseEntity<String> sendEmail(EmailSendingRequest emailSendingRequest) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			message.setContent(emailSendingRequest.getMessage(), "text/html");
			message.setFrom(fromEmail);

			MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

			helper.setTo(emailSendingRequest.getToEmail());
			if (emailSendingRequest.getCcEmail() != null && !emailSendingRequest.getCcEmail().isEmpty()) {
				helper.setCc(emailSendingRequest.getCcEmail());
			}
			helper.setSubject(emailSendingRequest.getSubject());

			emailSender.send(message);
		} catch (Exception e) {
			logger.error("E-mail sending failed. Payload {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send e-mail");
		}
		return ResponseEntity.status(HttpStatus.OK).body("E-mail sent successfully");
	}
}
