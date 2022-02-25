package com.grouptwo.soccer.transfers.teams.converters;

import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.grouptwo.soccer.transfers.lib.responses.TeamResponse;
import com.grouptwo.soccer.transfers.teams.models.Team;

@Component
public class TeamConverter {

	private final PlayerConverter playerConverter;

	public TeamConverter(PlayerConverter playerConverter) {
		this.playerConverter = playerConverter;
	}

	public TeamResponse fromEntityToResponse(Team team) {
		TeamResponse response = new TeamResponse();
		BeanUtils.copyProperties(team, response);

		if (team.getPlayers() != null) {
			response.setPlayers(team.getPlayers().stream().map(p -> this.playerConverter.fromEntityToResponse(p)).collect(Collectors.toSet()));
		}
		return response;
	}

	public Team fromResponseToEntity(TeamResponse teamResponse) {
		Team entity = new Team();
		BeanUtils.copyProperties(teamResponse, entity);
		return entity;
	}
}
