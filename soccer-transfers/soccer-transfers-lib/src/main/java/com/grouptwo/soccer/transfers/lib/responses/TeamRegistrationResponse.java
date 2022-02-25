package com.grouptwo.soccer.transfers.lib.responses;

import java.util.UUID;

public class TeamRegistrationResponse extends DefaultTeamResponse {

	public TeamRegistrationResponse(UUID teamId) {
		this.teamId = teamId;
	}

	@Override
	public String toString() {
		return "TeamRegistrationResponse [" + super.toString() + "]";
	}
}
