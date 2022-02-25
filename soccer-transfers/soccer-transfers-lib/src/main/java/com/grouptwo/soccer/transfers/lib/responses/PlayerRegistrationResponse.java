package com.grouptwo.soccer.transfers.lib.responses;

import java.util.UUID;

public class PlayerRegistrationResponse extends DefaultPlayerResponse {

	public PlayerRegistrationResponse() {

	}

	public PlayerRegistrationResponse(UUID teamId, UUID playerId) {
		this.teamId = teamId;
		this.playerId = playerId;
	}

	@Override
	public String toString() {
		return "PlayerRegistrationResponse [" + super.toString() + "]";
	}
}
