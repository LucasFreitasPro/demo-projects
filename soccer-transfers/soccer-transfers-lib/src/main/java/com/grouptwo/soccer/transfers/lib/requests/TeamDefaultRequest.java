package com.grouptwo.soccer.transfers.lib.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public abstract class TeamDefaultRequest {

	@NotBlank(message = "Name must not be null or empty")
	protected String name;

	@NotBlank(message = "State must not be null or empty")
	@Size(min = 2, max = 2, message = "State accepts only two characters")
	protected String state;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
