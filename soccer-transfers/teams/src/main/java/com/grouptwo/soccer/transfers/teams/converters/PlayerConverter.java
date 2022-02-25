package com.grouptwo.soccer.transfers.teams.converters;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.grouptwo.soccer.transfers.lib.requests.PlayerRegistrationRequest;
import com.grouptwo.soccer.transfers.lib.responses.PlayerResponse;
import com.grouptwo.soccer.transfers.teams.models.Player;

@Component
public class PlayerConverter {

	public PlayerResponse fromEntityToResponse(Player entity) {
		PlayerResponse response = new PlayerResponse();
		BeanUtils.copyProperties(entity, response);
		return response;
	}

	public Player fromResponseToEntity(PlayerResponse response) {
		Player entity = new Player();
		BeanUtils.copyProperties(response, entity);
		return entity;
	}

	public Player fromRequestToEntity(PlayerRegistrationRequest request) {
		Player entity = new Player();
		BeanUtils.copyProperties(request, entity);
		return entity;
	}
}
