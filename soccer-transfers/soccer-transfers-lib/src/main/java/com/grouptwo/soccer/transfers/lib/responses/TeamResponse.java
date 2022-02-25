package com.grouptwo.soccer.transfers.lib.responses;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "teamId", "name", "state", "country", "players" })
public class TeamResponse extends DefaultTeamResponse {

	private String name;

	private String state;

	private String country;

	private Set<PlayerResponse> players;

	@JsonIgnore
	private boolean deleted;

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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set<PlayerResponse> getPlayers() {
		return players;
	}

	public void setPlayers(Set<PlayerResponse> players) {
		this.players = players;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "TeamResponse [" + super.toString() + ", name=" + name + ", state=" + state + ", country=" + country + ", players=" + players + "]";
	}
}
