package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.Objects;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		BigDecimal balance = accountService.getAccount(currentUser).getBalance();
        System.out.println("Your current account balance is: " + balance);
	}

	private void viewTransferHistory() {
		Transfer[] transfers = transferService.getTransfersForUser(currentUser);

        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID\t\t\tFrom/To\t\tAmount");
        System.out.println("-------------------------------------------");
        for (Transfer t : transfers) {
            String fromOrTo = Objects.equals(t.getUserFrom().getId(), currentUser.getUser().getId()) ? "To" : "From";
            String otherUser = Objects.equals(t.getUserFrom().getId(), currentUser.getUser().getId()) ?
                    t.getUserTo().getUsername() : t.getUserFrom().getUsername();

            System.out.println(t.getTransferId() + "\t\t" + fromOrTo + " " +
                    otherUser + "\t\t" + t.getAmount());
        }
        System.out.println("-------------------------------------------");
	}


	private void viewPendingRequests() {

        Transfer[] transfers = transferService.getPendingRequests(currentUser);

        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID\t\t\tTo\t\tAmount");
        System.out.println("-------------------------------------------");


        int[] options = new int[transfers.length];
        for (int i = 0; i < transfers.length; i++) {
            options[i] = transfers[i].getTransferId();
            System.out.println(transfers[i].getTransferId() + "\t\t" +
                    transfers[i].getUserFrom().getUsername() + "\t\t" + transfers[i].getAmount());
        }

        int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel):", options);

        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("---------");

        int selection = consoleService.promptForInt("Please choose an option:", 0, 2);


        if (selection == 1) {
            if (transferService.approvePendingRequest(currentUser, transferId)) {
                System.out.println("success!");
            } else {
                System.out.println("failed!");
            }
        } else if (selection == 2) {
            if (transferService.rejectPendingRequest(currentUser, transferId)) {
                System.out.println("success!");
            } else {
                System.out.println("failed!");
            }
        }
	}

	private void sendBucks() {

        User[] users = userService.getOtherUsers(currentUser);

        System.out.println("-------------------------------------------");
        System.out.println("ID\t\t\tName");
        System.out.println("-------------------------------------------");

        int[] options = new int[users.length];
        for (int i = 0; i < users.length; i++) {
            options[i] = Math.toIntExact(users[i].getId());

            System.out.println(users[i].getId() + "\t\t" + users[i].getUsername());
        }

        System.out.println("-------------------------------------------");
        System.out.println();

        int recipientUserId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):", options);
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount:", true);

        TransferDTO transfer = new TransferDTO(Math.toIntExact(currentUser.getUser().getId()), recipientUserId, amount);
        if (transferService.sendBucks(currentUser, transfer)) {
            System.out.println("success!");
        } else {
            System.out.println("failed!");
        }
	}

	private void requestBucks() {
        User[] users = userService.getOtherUsers(currentUser);

        System.out.println("-------------------------------------------");
        System.out.println("ID\t\t\tName");
        System.out.println("-------------------------------------------");

        int[] options = new int[users.length];
        for (int i = 0; i < users.length; i++) {
            options[i] = Math.toIntExact(users[i].getId());
            System.out.println(users[i].getId() + "\t\t" + users[i].getUsername());
        }

        System.out.println("-------------------------------------------");
        System.out.println();

        int targetUserId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel):", options);
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount:", true);

        TransferDTO transfer = new TransferDTO(targetUserId, Math.toIntExact(currentUser.getUser().getId()), amount);
        if (transferService.sendBucks(currentUser, transfer)) {
            System.out.println("success!");
        } else {
            System.out.println("failed!");
        }
		
	}

}
