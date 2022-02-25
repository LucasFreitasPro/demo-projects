package com.grouptwo.soccer.transfers.lib.responses;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class DefaultPlayerResponse {

	@JsonProperty("team-id")
	protected UUID teamId;

	@JsonProperty("player-id")
	protected UUID playerId;

	public UUID getTeamId() {
		return teamId;
	}

	public void setTeamId(UUID teamId) {
		this.teamId = teamId;
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public void setPlayerId(UUID playerId) {
		this.playerId = playerId;
	}

	@Override
	public String toString() {
		return "playerId=" + playerId + ", teamId=" + teamId;
	}
}
