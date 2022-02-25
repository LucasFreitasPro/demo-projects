package com.grouptwo.soccer.transfers.teams.models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "player")
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID playerId;

	@Column(nullable = false, unique = true, length = 255)
	private String name;

	@Column(nullable = false)
	private LocalDate birth;

	@Column(nullable = false, length = 100)
	private String country;

	// mesmo com a diretiva FetchType.LAZY a lib Jackson ao invocar o método GET faz
	// um EAGER. Por isso, JsonIgnore está sendo utilizado para que o método GET não
	// seja chamado
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "team_id", referencedColumnName = "team_id", nullable = false)
	private Team team;

	public UUID getPlayerId() {
		return playerId;
	}

	public void setPlayerId(UUID playerId) {
		this.playerId = playerId;
	}

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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
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
		Player other = (Player) obj;
		return Objects.equals(name, other.name);
	}
}
