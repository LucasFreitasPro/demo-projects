package com.grouptwo.email.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grouptwo.email.services.EmailSendingService;
import com.grouptwo.soccer.transfers.lib.requests.EmailSendingRequest;

@RestController
@RequestMapping(path = "/api/v1/email")
public class EmailSendingController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final EmailSendingService emailSendingService;

	public EmailSendingController(EmailSendingService emailSendingService) {
		this.emailSendingService = emailSendingService;
	}

	@PostMapping
	public ResponseEntity<String> sendEmail(@RequestBody @Valid EmailSendingRequest emailSendingRequest) {
		logger.info("New e-mail sending request {}", emailSendingRequest);

		ResponseEntity<String> responseEntity = this.emailSendingService.sendEmail(emailSendingRequest);

		logger.info(responseEntity.getBody() + " {}", emailSendingRequest);

		return responseEntity;
	}
}
