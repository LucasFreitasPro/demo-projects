package com.grouptwo.soccer.transfers.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grouptwo.soccer.transfers.models.Transfer;
import com.grouptwo.soccer.transfers.repositories.TransferRepository;

@Service
public class TransferService {

	private TransferRepository repository;

	public TransferService(TransferRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void save(Transfer newTransfer) {
		this.repository.save(newTransfer);
	}
}
