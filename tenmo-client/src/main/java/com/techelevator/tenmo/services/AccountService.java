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
    private final ConsoleService consoleService;

    public AccountService(String baseUrl, ConsoleService consoleService) {
        this.baseUrl = baseUrl;
        this.consoleService = consoleService;
    }



    public Account getAccount(AuthenticatedUser authenticatedUser) {
        Account account = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "account", HttpMethod.GET, entity, Account.class);
            if (response.hasBody()) {
                account = response.getBody();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            consoleService.printErrorMessage(e.getMessage());
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public BigDecimal getBalance(AuthenticatedUser authenticatedUser) {

        return null;


    }





}
