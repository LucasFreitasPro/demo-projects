package com.grouptwo.soccer.transfers.lib.requests;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public abstract class PlayerDefaultRequest {

	@NotBlank(message = "Name must not be null or empty")
	protected String name;

	@NotNull(message = "Birth must not be null")
	protected LocalDate birth;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}
}
