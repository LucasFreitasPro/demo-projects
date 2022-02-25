package com.grouptwo.soccer.transfers.lib.requests;

import javax.validation.constraints.NotBlank;

public class TeamRegistrationRequest extends TeamDefaultRequest {

	@NotBlank(message = "Country must not be null or empty")
	private String country;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "TeamRegistrationRequest [name=" + name + ", state=" + state + ", country=" + country + "]";
	}
}
