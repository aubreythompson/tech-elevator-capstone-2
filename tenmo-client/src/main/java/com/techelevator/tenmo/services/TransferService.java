package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;

    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Transfer[] getTransfersForUser(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers", HttpMethod.GET, entity, Transfer[].class);
            if (response.hasBody()) {
                transfers = response.getBody();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public boolean makeTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        try {
            ResponseEntity<Void> ignored = restTemplate.exchange(baseUrl + "make-transfer", HttpMethod.POST, entity, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
    }


    public boolean sendBucks(AuthenticatedUser authenticatedUser, TransferDTO transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<TransferDTO> entity = new HttpEntity<>(transfer, headers);

        try {
            restTemplate.exchange(baseUrl + "send-bucks", HttpMethod.POST, entity, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
    }

    public boolean requestBucks(AuthenticatedUser authenticatedUser, TransferDTO transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<TransferDTO> entity = new HttpEntity<>(transfer, headers);

        try {
            restTemplate.exchange(baseUrl + "request-bucks", HttpMethod.POST, entity, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
    }


    public Transfer[] getPendingRequests(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "pending-requests", HttpMethod.GET, entity, Transfer[].class);
            if (response.hasBody()) {
                transfers = response.getBody();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;

    }

    public boolean approvePendingRequest(AuthenticatedUser authenticatedUser, int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(baseUrl + "approve-request/" + transferId, HttpMethod.POST, entity, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }

    }

    public boolean rejectPendingRequest(AuthenticatedUser authenticatedUser, int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(baseUrl + "reject-request/" + transferId, HttpMethod.POST, entity, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
    }








}
