package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
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
}