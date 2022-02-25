package com.grouptwo.soccer.transfers.requests;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRequest {

	@JsonProperty("player-id")
	@NotNull
	private UUID playerId;

	@JsonProperty("from-team-id")
	@NotNull
	private UUID fromTeamId;

	@JsonProperty("to-team-id")
	@NotNull
	private UUID toTeamId;

	@JsonProperty("transfer-value")
	@Positive
	private Double transferValue;

	public UUID getPlayerId() {
		return playerId;
	}

	public void setPlayerId(UUID playerId) {
		this.playerId = playerId;
	}

	public UUID getFromTeamId() {
		return fromTeamId;
	}

	public void setFromTeamId(UUID fromTeamId) {
		this.fromTeamId = fromTeamId;
	}

	public UUID getToTeamId() {
		return toTeamId;
	}

	public void setToTeamId(UUID toTeamId) {
		this.toTeamId = toTeamId;
	}

	public Double getTransferValue() {
		return transferValue;
	}

	public void setTransferValue(Double transferValue) {
		this.transferValue = transferValue;
	}

	@Override
	public String toString() {
		return "TransferRequest [playerId=" + playerId + ", fromTeamId=" + fromTeamId + ", toTeamId=" + toTeamId + ", transferValue=" + transferValue + "]";
	}
}
