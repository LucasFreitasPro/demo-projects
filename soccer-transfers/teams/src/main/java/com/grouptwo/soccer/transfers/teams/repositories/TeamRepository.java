package com.grouptwo.soccer.transfers.teams.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grouptwo.soccer.transfers.teams.models.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {

	public boolean existsByName(String name);

	public Optional<Team> findByName(String teamName);
}
