package com.grouptwo.soccer.transfers.lib.requests;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerUpdateRequest extends PlayerDefaultRequest {

	@JsonProperty("team-id")
	private UUID teamId;

	public UUID getTeamId() {
		return teamId;
	}

	public void setTeamId(UUID teamId) {
		this.teamId = teamId;
	}

	@Override
	public String toString() {
		return "PlayerUpdateRequest [name=" + name + ", birth=" + birth + "]";
	}
}
