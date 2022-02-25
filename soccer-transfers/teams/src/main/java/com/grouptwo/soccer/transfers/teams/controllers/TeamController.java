package com.grouptwo.soccer.transfers.teams.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grouptwo.soccer.transfers.lib.requests.TeamRegistrationRequest;
import com.grouptwo.soccer.transfers.lib.requests.TeamUpdateRequest;
import com.grouptwo.soccer.transfers.lib.responses.ErrorOrConflictResponse;
import com.grouptwo.soccer.transfers.lib.responses.TeamRegistrationResponse;
import com.grouptwo.soccer.transfers.lib.responses.TeamResponse;
import com.grouptwo.soccer.transfers.teams.models.Team;
import com.grouptwo.soccer.transfers.teams.services.TeamService;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final TeamService service;

	public TeamController(TeamService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Object> register(@RequestBody @Valid TeamRegistrationRequest teamRegistrationRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorOrConflictResponse(allErrors.stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList()).toString().replace("[", "").replace("]", "")));
		}

		teamRegistrationRequest.setName(teamRegistrationRequest.getName().toUpperCase());

		logger.info("new team registration {}", teamRegistrationRequest);

		if (this.service.existsByName(teamRegistrationRequest.getName())) {
			logger.warn("Team is already registered {}", teamRegistrationRequest);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("Team is already registered"));
		} else {
			Team newTeam = new Team(Boolean.FALSE);
			BeanUtils.copyProperties(teamRegistrationRequest, newTeam);
			newTeam = this.service.save(newTeam);

			TeamRegistrationResponse registrationResponse = new TeamRegistrationResponse(newTeam.getTeamId());
			logger.info("Team registered successfully {}", registrationResponse);
			return ResponseEntity.status(HttpStatus.OK).body(registrationResponse);
		}
	}

	@PostMapping(path = "/list")
	public ResponseEntity<String> registerList(@RequestBody List<TeamRegistrationRequest> teams, BindingResult bindingResult) {
		for (TeamRegistrationRequest teamRegistrationRequest : teams) {
			register(teamRegistrationRequest, bindingResult);
		}

		return ResponseEntity.status(HttpStatus.OK).body("All teams registered");
	}

	@GetMapping
	public ResponseEntity<Page<TeamResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		logger.info("get all teams");
		Page<TeamResponse> all = this.service.findAll(pageable);
		logger.info("list of retrivered teams {}", all.getContent());
		return ResponseEntity.status(all != null && !all.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(all);
	}

	@GetMapping(path = "/{teamId}")
	public ResponseEntity<Object> getOne(@PathVariable("teamId") UUID teamId) {
		logger.info("get one team {}", teamId);

		TeamResponse teamResponse = this.service.findById(teamId);
		if (teamResponse != null) {
			logger.info("retrivered team {}", teamResponse);
			return ResponseEntity.status(HttpStatus.OK).body(teamResponse);
		} else {
			logger.warn("team not found {}", teamId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorOrConflictResponse("Team not found"));
		}
	}

	@PatchMapping(path = "/{teamId}")
	public ResponseEntity<Object> updateOne(@PathVariable("teamId") UUID teamId, @RequestBody @Valid TeamUpdateRequest teamUpdateRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorOrConflictResponse(bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList()).toString().replace("[", "").replace("]", "")));
		}

		logger.info("updating team {}. Payload {}", teamId, teamUpdateRequest);

		teamUpdateRequest.setName(teamUpdateRequest.getName().toUpperCase());

		Optional<Team> teamOptionalByName = this.service.findByName(teamUpdateRequest.getName());
		if (teamOptionalByName.isPresent() && !teamOptionalByName.get().getTeamId().equals(teamId)) {
			logger.warn("The given name is already in use on another team. Payload {} ", teamUpdateRequest);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("The given name is already in use on another team"));
		}

		TeamResponse teamResponse = this.service.findById(teamId);
		if (teamResponse != null) {
			teamResponse.setName(teamUpdateRequest.getName());
			teamResponse.setState(teamUpdateRequest.getState());
			Team saved = this.service.save(teamResponse);
			teamResponse.setTeamId(saved.getTeamId());
			logger.info("Team updated successfully {} ", teamUpdateRequest);
			return ResponseEntity.status(HttpStatus.OK).body(teamResponse);
		} else {
			logger.warn("Team not found {} ", teamUpdateRequest);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("Team not found"));
		}
	}

	@DeleteMapping(path = "/{teamId}")
	public ResponseEntity<Object> deleteOne(@PathVariable("teamId") UUID teamId) {
		logger.info("deleting team {}", teamId);

		TeamResponse teamResponse = this.service.findById(teamId);
		if (teamResponse != null) {
			teamResponse.setDeleted(true);
			this.service.save(teamResponse);
			logger.info("Team deleted successfully. ID {}", teamId);
			return ResponseEntity.status(HttpStatus.OK).body("Team deleted successfully");
		} else {
			logger.warn("Team not found. ID {}", teamId);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorOrConflictResponse("Team not found"));
		}
	}
}
