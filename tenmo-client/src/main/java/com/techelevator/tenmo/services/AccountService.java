package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }



    public Account getAccount(AuthenticatedUser authenticatedUser) {
        Account account = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Long> entity = new HttpEntity<>(authenticatedUser.getUser().getId(), headers);

        try {
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "account", HttpMethod.POST, entity, Account.class);
            if (response.hasBody()) {
                account = response.getBody();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public BigDecimal getBalance(AuthenticatedUser authenticatedUser) {

        return null;


    }





}
