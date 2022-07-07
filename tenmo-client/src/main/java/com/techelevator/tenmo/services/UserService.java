package com.techelevator.tenmo.services;

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

public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;

    public UserService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUserName(int userId) {
        String userName = null;

        try {
            userName = restTemplate.getForObject(baseUrl + "users/" + userId, String.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return userName;
    }

    public String getUserNameByAccount(int accountId) {
        String userName = null;

        try {
            userName = restTemplate.postForObject(baseUrl + "get-user", accountId, String.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return userName;
    }


}
