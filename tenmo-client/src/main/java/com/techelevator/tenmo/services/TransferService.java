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
    private final ConsoleService consoleService;

    public TransferService(String baseUrl, ConsoleService consoleService) {
        this.baseUrl = baseUrl;
        this.consoleService = consoleService;
    }

    /***
     * GET method for getting all transfers TO and FROM a user.
     * Note: this includes even those that were rejected or are pending.
     * @param authenticatedUser
     * @return list of transfers for user
     */
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
            consoleService.printErrorMessage(e.getMessage());
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
            consoleService.printErrorMessage(e.getMessage());
            BasicLogger.log(e.getMessage());
            return false;
        }
    }

    /***
     * This method transfers funds from one account to the other
     * called when a send is made
     * @param authenticatedUser
     * @param transfer
     * @return boolean successful transfer made
     */
    public boolean sendBucks(AuthenticatedUser authenticatedUser, TransferDTO transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<TransferDTO> entity = new HttpEntity<>(transfer, headers);

        try {
            restTemplate.exchange(baseUrl + "send-bucks", HttpMethod.POST, entity, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            consoleService.printErrorMessage(e.getMessage());
            BasicLogger.log(e.getMessage());
            return false;
        }
    }

    /***
     * This creates a type: request, status: pending transfer
     * with a POST method
     * @param authenticatedUser
     * @param transfer
     * @return boolean successful request made
     */
    public boolean requestBucks(AuthenticatedUser authenticatedUser, TransferDTO transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<TransferDTO> entity = new HttpEntity<>(transfer, headers);

        try {
            restTemplate.exchange(baseUrl + "request-bucks", HttpMethod.POST, entity, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            consoleService.printErrorMessage(e.getMessage());
            BasicLogger.log(e.getMessage());
            return false;
        }
    }

    /***
     *This gets the pending requests FROM the authenticated user
     * @param authenticatedUser
     * @return
     */

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
            consoleService.printErrorMessage(e.getMessage());
            BasicLogger.log(e.getMessage());
        }
        return transfers;

    }

    /***
     * Turns a pending request status to approved from the authenticated user
     * @param authenticatedUser
     * @param transferId
     * @return boolean successful approve made
     */
    public boolean approvePendingRequest(AuthenticatedUser authenticatedUser, int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(baseUrl + "approve-request/" + transferId, HttpMethod.PUT, entity, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            consoleService.printErrorMessage(e.getMessage());
            BasicLogger.log(e.getMessage());
            return false;
        }

    }

    /***
     * Turns a pending request status to rejected from the authenticated user
     * @param authenticatedUser
     * @param transferId
     * @return boolean successful reject made
     */
    public boolean rejectPendingRequest(AuthenticatedUser authenticatedUser, int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(baseUrl + "reject-request/" + transferId, HttpMethod.PUT, entity, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            consoleService.printErrorMessage(e.getMessage());
            BasicLogger.log(e.getMessage());
            return false;
        }
    }

}
