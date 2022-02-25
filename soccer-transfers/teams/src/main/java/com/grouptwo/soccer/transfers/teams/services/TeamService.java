package com.grouptwo.soccer.transfers.teams.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grouptwo.soccer.transfers.lib.responses.TeamResponse;
import com.grouptwo.soccer.transfers.teams.converters.TeamConverter;
import com.grouptwo.soccer.transfers.teams.models.Team;
import com.grouptwo.soccer.transfers.teams.repositories.TeamRepository;

@Service
public class TeamService {

	private TeamRepository repository;

	private TeamConverter converter;

	public TeamService(TeamRepository repository, TeamConverter converter) {
		this.repository = repository;
		this.converter = converter;
	}

	public TeamConverter getConverter() {
		return converter;
	}

	@Transactional
	public Team save(Team newTeam) {
		return this.repository.save(newTeam);
	}

	public Team save(TeamResponse teamResponse) {
		return save(this.converter.fromResponseToEntity(teamResponse));
	}

	public Page<TeamResponse> findAll(Pageable pageable) {
		Page<TeamResponse> teams = null;

		Page<Team> all = this.repository.findAll(pageable);
		if (all != null && !all.isEmpty()) {
			List<TeamResponse> list = all.stream().map(t -> this.converter.fromEntityToResponse(t)).collect(Collectors.toList());
			teams = new PageImpl<TeamResponse>(list);
		}
		return teams;
	}

	public TeamResponse findById(UUID id) {
		Optional<Team> optional = this.repository.findById(id);
		if (optional.isPresent()) {
			return this.converter.fromEntityToResponse(optional.get());
		}
		return null;
	}

	public boolean existsByName(String name) {
		return this.repository.existsByName(name);
	}

	public Optional<Team> findByName(String teamName) {
		return this.repository.findByName(teamName);
	}
}
