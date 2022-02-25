package com.grouptwo.soccer.transfers.lib.responses;

public class ErrorOrConflictResponse {

	private String message;

	public ErrorOrConflictResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
