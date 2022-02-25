package com.grouptwo.soccer.transfers.teams.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grouptwo.soccer.transfers.lib.responses.PlayerResponse;
import com.grouptwo.soccer.transfers.teams.converters.PlayerConverter;
import com.grouptwo.soccer.transfers.teams.models.Player;
import com.grouptwo.soccer.transfers.teams.repositories.PlayerRepository;

@Service
public class PlayerService {

	private PlayerRepository repository;

	private PlayerConverter converter;

	public PlayerService(PlayerRepository repository, PlayerConverter converter) {
		this.repository = repository;
		this.converter = converter;
	}

	public PlayerConverter getConverter() {
		return converter;
	}

	@Transactional
	public Player save(Player newPlayer) {
		return this.repository.save(newPlayer);
	}

	public Optional<Player> findById(UUID id) {
		return this.repository.findById(id);
	}

	public boolean existsByName(String name) {
		return this.repository.existsByName(name);
	}

	public void delete(Player player) {
		this.repository.delete(player);
	}

	public List<PlayerResponse> findByTeamId(UUID id) {
		return this.repository.findByTeamId(id).stream().map(p -> this.converter.fromEntityToResponse(p)).collect(Collectors.toList());
	}

	public Player getByTeamIdAndPlayerId(UUID teamId, UUID playerId) {
		return this.repository.getByTeamIdAndPlayerId(teamId, playerId);
	}

	public Player findByName(String name) {
		return this.repository.findByName(name);
	}
}
