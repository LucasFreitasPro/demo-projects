package com.grouptwo.soccer.transfers.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grouptwo.soccer.transfers.models.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {

}
