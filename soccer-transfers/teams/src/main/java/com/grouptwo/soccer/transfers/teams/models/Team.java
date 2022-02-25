package com.grouptwo.soccer.transfers.teams.models;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "team")
@Where(clause = "deleted = 'f'")
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "team_id")
	private UUID teamId;

	@Column(nullable = false, unique = true, length = 255)
	private String name;

	@Column(nullable = false, length = 2)
	private String state;

	@Column(nullable = false, length = 100)
	private String country;

	@OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
	private Set<Player> players;

	@Column(nullable = false)
	private Boolean deleted;

	public Team() {

	}

	public Team(Boolean deleted) {
		this.deleted = deleted;
	}

	public UUID getTeamId() {
		return teamId;
	}

	public void setTeamId(UUID teamId) {
		this.teamId = teamId;
	}

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

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		return Objects.equals(name, other.name);
	}
}
