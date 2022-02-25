package com.grouptwo.soccer.transfers.lib.requests;

import javax.validation.constraints.NotBlank;

public class PlayerRegistrationRequest extends PlayerDefaultRequest {

	@NotBlank
	private String country;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "PlayerRegistrationRequest [name=" + name + ", birth=" + birth + ", country=" + country + "]";
	}
}
