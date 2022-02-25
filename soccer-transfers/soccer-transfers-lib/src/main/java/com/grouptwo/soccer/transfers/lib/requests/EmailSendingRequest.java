package com.grouptwo.soccer.transfers.lib.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailSendingRequest {

	@Email
	@JsonProperty("to-email")
	@NotBlank
	private String toEmail;

	@NotBlank
	private String subject;

	@NotBlank
	private String message;

	@JsonProperty("cc-email")
	private String ccEmail;

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "EmailSendingRequest [toEmail=" + toEmail + ", subject=" + subject + ", message=" + message + "]";
	}

	public String getCcEmail() {
		return this.ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}
}
