package com.grouptwo.soccer.transfers.models;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transfer")
public class Transfer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private UUID playerId;

	@Column(nullable = false)
	private UUID fromTeamId;

	@Column(nullable = false)
	private UUID toTeamId;

	@Column(nullable = false)
	private LocalDateTime transferredAt;

	@Column(nullable = false)
	private Double transferValue;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	public LocalDateTime getTransferredAt() {
		return transferredAt;
	}

	public void setTransferredAt(LocalDateTime transferredAt) {
		this.transferredAt = transferredAt;
	}

	public Double getTransferValue() {
		return transferValue;
	}

	public void setTransferValue(Double transferValue) {
		this.transferValue = transferValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromTeamId == null) ? 0 : fromTeamId.hashCode());
		result = prime * result + ((playerId == null) ? 0 : playerId.hashCode());
		result = prime * result + ((toTeamId == null) ? 0 : toTeamId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transfer other = (Transfer) obj;
		if (fromTeamId == null) {
			if (other.fromTeamId != null)
				return false;
		} else if (!fromTeamId.equals(other.fromTeamId))
			return false;
		if (playerId == null) {
			if (other.playerId != null)
				return false;
		} else if (!playerId.equals(other.playerId))
			return false;
		if (toTeamId == null) {
			if (other.toTeamId != null)
				return false;
		} else if (!toTeamId.equals(other.toTeamId))
			return false;
		return true;
	}
}
