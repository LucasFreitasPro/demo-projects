package com.grouptwo.soccer.transfers.teams.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grouptwo.soccer.transfers.lib.requests.PlayerRegistrationRequest;
import com.grouptwo.soccer.transfers.lib.requests.PlayerUpdateRequest;
import com.grouptwo.soccer.transfers.lib.responses.ErrorOrConflictResponse;
import com.grouptwo.soccer.transfers.lib.responses.PlayerRegistrationResponse;
import com.grouptwo.soccer.transfers.lib.responses.PlayerResponse;
import com.grouptwo.soccer.transfers.lib.responses.TeamResponse;
import com.grouptwo.soccer.transfers.teams.models.Player;
import com.grouptwo.soccer.transfers.teams.services.PlayerService;
import com.grouptwo.soccer.transfers.teams.services.TeamService;

@RestController
@RequestMapping(path = "/api/v1/teams/{teamId}/players")
public class PlayerController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private TeamService teamService;

	private PlayerService playerService;

	public PlayerController(TeamService teamService, PlayerService playerService) {
		super();
		this.teamService = teamService;
		this.playerService = playerService;
	}

	@PostMapping
	public ResponseEntity<Object> register(@PathVariable("teamId") UUID teamId, @RequestBody @Valid PlayerRegistrationRequest playerRegistrationRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorOrConflictResponse(bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList()).toString().replace("[", "").replace("]", "")));
		}

		logger.info("new player registration {}", playerRegistrationRequest);

		playerRegistrationRequest.setName(playerRegistrationRequest.getName().toUpperCase());

		TeamResponse teamResponse = this.teamService.findById(teamId);
		if (teamResponse != null) {
			if (this.playerService.existsByName(playerRegistrationRequest.getName())) {
				logger.warn("Player already registered. Payload {}", playerRegistrationRequest);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("Player already registered"));
			} else {
				Player player = this.playerService.getConverter().fromRequestToEntity(playerRegistrationRequest);
				player.setTeam(this.teamService.getConverter().fromResponseToEntity(teamResponse));
				player = this.playerService.save(player);

				PlayerRegistrationResponse playerRegistrationResponse = new PlayerRegistrationResponse(teamId, player.getPlayerId());
				logger.info("Player registered successfully {}", playerRegistrationResponse);
				return ResponseEntity.status(HttpStatus.OK).body(playerRegistrationResponse);
			}
		} else {
			logger.warn("Given team not found. Team {}", teamId);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("Given team not found"));
		}
	}

	@GetMapping
	public ResponseEntity<List<PlayerResponse>> getAll(@PathVariable("teamId") UUID teamId) {
		logger.info("get all players from team: {}", teamId);

		List<PlayerResponse> players = this.playerService.findByTeamId(teamId);

		logger.info("list of retrivered players {}", players);

		return ResponseEntity.status(players != null && !players.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(players);
	}

	@GetMapping(path = "/{playerId}")
	public ResponseEntity<Object> getOne(@PathVariable("teamId") UUID teamId, @PathVariable("playerId") UUID playerId) {
		logger.info("get player {} from team {} ", playerId, teamId);

		Player player = this.playerService.getByTeamIdAndPlayerId(teamId, playerId);
		if (player != null) {
			PlayerResponse playerResponse = new PlayerResponse();
			BeanUtils.copyProperties(player, playerResponse);
			playerResponse.setTeamId(player.getTeam().getTeamId());

			logger.info("retrivered player {}", playerResponse);
			return ResponseEntity.status(HttpStatus.OK).body(playerResponse);
		} else {
			logger.warn("Player not found. Team {} Player {}", teamId, playerId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorOrConflictResponse("Player not found"));
		}
	}

	@PatchMapping(path = "/{playerId}")
	public ResponseEntity<Object> updateOne(@PathVariable("teamId") UUID teamId, @PathVariable("playerId") UUID playerId, @RequestBody @Valid PlayerUpdateRequest playerUpdateRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorOrConflictResponse(bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList()).toString().replace("[", "").replace("]", "")));
		}

		logger.info("updating player {} from team {}. Payload: ", playerId, teamId, playerUpdateRequest);

		playerUpdateRequest.setName(playerUpdateRequest.getName().toUpperCase());

		Player playerByName = this.playerService.findByName(playerUpdateRequest.getName());
		if (playerByName != null && !playerByName.getTeam().getTeamId().equals(teamId)) {
			logger.warn("The given name is already in use by another player. Payload {}", playerUpdateRequest);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("The given name is already in use by another player"));
		}

		if (playerUpdateRequest.getTeamId() == null) {
			playerUpdateRequest.setTeamId(teamId);
		}

		TeamResponse teamResponse = this.teamService.findById(playerUpdateRequest.getTeamId());
		if (teamResponse != null) {
			Optional<Player> playerOptional = this.playerService.findById(playerId);
			if (playerOptional.isPresent()) {
				Player player = playerOptional.get();
				player.setName(playerUpdateRequest.getName());
				player.setBirth(playerUpdateRequest.getBirth());
				player.setTeam(this.teamService.getConverter().fromResponseToEntity(teamResponse));

				PlayerResponse pr = this.playerService.getConverter().fromEntityToResponse(this.playerService.save(player));
				logger.info("player updated successfully {}", pr);

				return ResponseEntity.status(HttpStatus.OK).body(pr);
			} else {
				logger.warn("Player not found. Payload {}", playerUpdateRequest);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("Player not found"));
			}
		} else {
			logger.warn("Given team not found. Payload {}", playerUpdateRequest);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("Given team not found"));
		}
	}

	@DeleteMapping(path = "/{playerId}")
	public ResponseEntity<Object> deleteOne(@PathVariable("teamId") UUID teamId, @PathVariable("playerId") UUID playerId) {
		logger.info("deleting player {} from team {} ", playerId, teamId);

		TeamResponse teamResponse = this.teamService.findById(teamId);
		if (teamResponse != null) {
			Optional<Player> playerOptional = this.playerService.findById(playerId);
			if (playerOptional.isPresent()) {
				this.playerService.delete(playerOptional.get());

				logger.info("Player deleted successfully. team {} player {}", teamId, playerId);
				return ResponseEntity.status(HttpStatus.OK).body("Player deleted successfully");
			} else {
				logger.warn("Player not found. Team {} Player {}", teamId, playerId);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("Player not found"));
			}
		} else {
			logger.warn("Associated team not found. Team {} Player {}", teamId, playerId);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("Associated team not found"));
		}
	}
}
