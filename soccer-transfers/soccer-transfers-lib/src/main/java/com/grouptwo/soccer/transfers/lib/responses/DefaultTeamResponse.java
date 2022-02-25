package com.grouptwo.soccer.transfers.lib.responses;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class DefaultTeamResponse {

	@JsonProperty("team-id")
	protected UUID teamId;

	public UUID getTeamId() {
		return teamId;
	}

	public void setTeamId(UUID teamId) {
		this.teamId = teamId;
	}

	@Override
	public String toString() {
		return "teamId=" + teamId;
	}
}
