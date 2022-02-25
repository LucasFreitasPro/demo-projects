package com.grouptwo.soccer.transfers.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.grouptwo.soccer.transfers.lib.requests.EmailSendingRequest;
import com.grouptwo.soccer.transfers.lib.requests.PlayerUpdateRequest;
import com.grouptwo.soccer.transfers.lib.responses.PlayerResponse;
import com.grouptwo.soccer.transfers.lib.responses.TeamResponse;
import com.grouptwo.soccer.transfers.models.Transfer;
import com.grouptwo.soccer.transfers.requests.TransferRequest;
import com.grouptwo.soccer.transfers.services.TransferService;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final TransferService service;

	private final RestTemplate restTemplate;

	private final String TEAMS_API = "http://TEAMS/api/v1/teams";
	private final String EMAIL_SENDING_API = "http://EMAIL-SENDING/api/v1/email";

	public TransferController(TransferService service, RestTemplate restTemplate) {
		this.service = service;
		this.restTemplate = restTemplate;
	}

	@PostMapping
	public ResponseEntity<Object> transferPlayer(@RequestBody @Valid TransferRequest transferRequest) {
		logger.info("new transfer {}", transferRequest);

		PlayerResponse playerResponse = null;
		try {
			playerResponse = restTemplate.getForObject(TEAMS_API + "/{teamId}/players/{playerId}", PlayerResponse.class, transferRequest.getFromTeamId(), transferRequest.getPlayerId());
		} catch (HttpClientErrorException e) {
			logger.error("Error calling TEAMS API", e);
			return ResponseEntity.status(HttpStatus.valueOf(e.getRawStatusCode())).body(e.getMessage());
		}

		if (playerResponse != null && playerResponse.getName() != null) {
			TeamResponse teamResponseTo = null;
			TeamResponse teamResponseFrom = null;
			try {
				teamResponseTo = restTemplate.getForObject(TEAMS_API + "/{teamId}", TeamResponse.class, transferRequest.getToTeamId());
			} catch (HttpClientErrorException e) {
				logger.error("Error calling TEAMS API", e);
				return ResponseEntity.status(HttpStatus.valueOf(e.getRawStatusCode())).body(e.getMessage());
			}

			try {
				teamResponseFrom = restTemplate.getForObject(TEAMS_API + "/{teamId}", TeamResponse.class, transferRequest.getFromTeamId());
			} catch (HttpClientErrorException e) {
				logger.error("Error calling TEAMS API", e);
				return ResponseEntity.status(HttpStatus.valueOf(e.getRawStatusCode())).body(e.getMessage());
			}

			if (teamResponseTo != null && teamResponseTo.getName() != null) {
				if (!transferRequest.getFromTeamId().equals(playerResponse.getTeamId())) {
					return ResponseEntity.status(HttpStatus.CONFLICT).body("Origin team does not match current team");
				}
				PlayerUpdateRequest playerUpdateRequest = new PlayerUpdateRequest();
				playerUpdateRequest.setBirth(playerResponse.getBirth());
				playerUpdateRequest.setName(playerResponse.getName());
				playerUpdateRequest.setTeamId(transferRequest.getToTeamId());

				try {
					restTemplate.put(TEAMS_API + "/{teamId}/players/{playerId}", playerUpdateRequest, transferRequest.getFromTeamId(), transferRequest.getPlayerId());
				} catch (HttpClientErrorException e) {
					logger.error("Error calling TEAMS API", e);
					return ResponseEntity.status(HttpStatus.valueOf(e.getRawStatusCode())).body(e.getMessage());
				}

				Transfer transfer = new Transfer();
				transfer.setPlayerId(transferRequest.getPlayerId());
				transfer.setToTeamId(transferRequest.getToTeamId());
				transfer.setFromTeamId(transferRequest.getFromTeamId());
				transfer.setTransferValue(transferRequest.getTransferValue());
				transfer.setTransferredAt(LocalDateTime.now(ZoneId.of("UTC")));
				this.service.save(transfer);

				logger.info("Player successfully transferred {}", transferRequest);

				sendEmailToOwner(playerResponse, teamResponseFrom, teamResponseTo, transferRequest);
				return ResponseEntity.status(HttpStatus.OK).body("Player successfully transferred");
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Destiny team not found");
			}
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Player not found");
		}
	}

	@GetMapping("/from-team/{teamId}")
	public ResponseEntity<Object> getAllFrom(@PathVariable("teamId") UUID teamId) {
		try {
			TeamResponse teamResponse = restTemplate.getForObject(TEAMS_API + "/{teamId}", TeamResponse.class, teamId);
			if (teamResponse != null) {

			}
			return ResponseEntity.status(HttpStatus.OK).body("");
		} catch (HttpClientErrorException e) {
			logger.error("Error calling TEAMS API", e);
			return ResponseEntity.status(HttpStatus.valueOf(e.getRawStatusCode())).body(e.getMessage());
		}
	}

	@GetMapping("/to-team/{teamId}")
	public void getAllTo(@PathVariable("teamId") UUID teamId) {

	}

	@GetMapping("/players/{playerId}")
	public ResponseEntity<Object> getAllPlayers(@PathVariable("playerId") UUID playerId) {
		return null;
	}

	private void sendEmailToOwner(PlayerResponse playerResponse, TeamResponse teamResponseFrom, TeamResponse teamResponseTo, TransferRequest transferRequest) {
		EmailSendingRequest emailSendingRequest = new EmailSendingRequest();
		try {
			emailSendingRequest.setSubject(String.format("SOCCER API - O jogador %s foi transferido", playerResponse.getName()));
			emailSendingRequest.setToEmail("email@gmail.com");

			StringBuilder message = new StringBuilder();
			message.append("<html>");
			message.append("	<body style='margin: auto; width: 60%'>");
			message.append("		<h2>Transferência realizada com sucesso!</h2><br></br>");
			message.append("		<h3>Jogador ").append(playerResponse.getName()).append("</h3><br>");
			message.append("		<p>");
			message.append("			<strong>Do</strong> ").append(teamResponseFrom.getName()).append(" <strong>para</strong> ").append(teamResponseTo.getName()).append("<br><br>");
			message.append("			<strong>Valor da transação:</strong>: R$ ").append(transferRequest.getTransferValue().intValue()).append(",00");
			message.append("		</p>");
			message.append("	</body>");
			message.append("</html>");

			emailSendingRequest.setMessage(message.toString());
			restTemplate.postForObject(EMAIL_SENDING_API, emailSendingRequest, String.class);
			logger.info("E-mail sending request sent successfully. Payload {}", emailSendingRequest);
		} catch (HttpClientErrorException e) {
			logger.error("Failed to send e-mail. Payload {}", emailSendingRequest, e);
		}
	}
}
