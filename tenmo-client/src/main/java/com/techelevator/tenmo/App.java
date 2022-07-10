package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.Objects;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL, consoleService);
    private final AccountService accountService = new AccountService(API_BASE_URL, consoleService);
    private final TransferService transferService = new TransferService(API_BASE_URL, consoleService);
    private final UserService userService = new UserService(API_BASE_URL, consoleService);

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
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
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
        Account account = accountService.getAccount(currentUser);
        if (account != null) {
            BigDecimal balance = account.getBalance();
            System.out.println("Your current account balance is: $" + balance);
        }
	}

	private void viewTransferHistory() {
		Transfer[] transfers = transferService.getTransfersForUser(currentUser);
        if (transfers == null) {
            return;
        }
        if (transfers.length == 0) {
            System.out.println("You have no past transfers to view");
            return;
        }

        System.out.println();
        System.out.println("1: Yes");
        System.out.println("2: No");
        System.out.println("---------");
        //see if the user wants to see pending/rejected transfers - 1 if yes, 2 if not
        boolean includeAllTransfers = 1 == consoleService.promptForInt("Would you like to include pending and rejected transfers?", 1, 2);

        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID\t\t\tFrom/To\t\tAmount");
        System.out.println("-------------------------------------------");

        int[] options = new int[transfers.length + 1];
        for (int i = 0; i < transfers.length; i++) {
            //skip pending and rejected transfers if desired
            if (!includeAllTransfers && (transfers[i].getStatusString().equals("Pending") || transfers[i].getStatusString().equals("Rejected"))) {
                continue;
            }

            options[i] = transfers[i].getTransferId();
            String fromOrTo = Objects.equals(transfers[i].getUserFrom().getId(), currentUser.getUser().getId()) ? "To" : "From";
            String otherUser = Objects.equals(transfers[i].getUserFrom().getId(), currentUser.getUser().getId()) ?
                    transfers[i].getUserTo().getUsername() : transfers[i].getUserFrom().getUsername();

            System.out.println(transfers[i].getTransferId() + "\t\t" + fromOrTo + " " +
                    otherUser + "\t\t$" + transfers[i].getAmount());
        }
        System.out.println("-------------------------------------------");
        int selection = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel):", options);
        if (selection == 0) {
            return;
        }

        System.out.println();
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");

        for (Transfer t : transfers) {
            if (t.getTransferId() == selection) {
                System.out.println("Id: " + t.getTransferId());
                System.out.println("From: " + t.getUserFrom().getUsername());
                System.out.println("To: " + t.getUserTo().getUsername());
                System.out.println("Type: " + t.getTypeString());
                System.out.println("Status: " + t.getStatusString());
                System.out.println("Amount: $" + t.getAmount());
                break;
            }
        }

	}

	private void viewPendingRequests() {

        Transfer[] transfers = transferService.getPendingRequests(currentUser);
        if (transfers == null) {
            return;
        }

        if (transfers.length == 0) {
            System.out.println("You have no pending requests to view");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID\t\t\tTo\t\tAmount");
        System.out.println("-------------------------------------------");


        int[] options = new int[transfers.length + 1];
        for (int i = 0; i < transfers.length; i++) {
            options[i] = transfers[i].getTransferId();
            System.out.println(transfers[i].getTransferId() + "\t\t" +
                    transfers[i].getUserTo().getUsername() + "\t\t$" + transfers[i].getAmount());
        }

        int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel):", options);
        if (transferId == 0) {
            return;
        }

        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("---------");

        int selection = consoleService.promptForInt("Please choose an option:", 0, 2);

        if (selection == 1) {
            if (transferService.approvePendingRequest(currentUser, transferId)) {
                System.out.println("successfully approved request!");
            }
        } else if (selection == 2) {
            if (transferService.rejectPendingRequest(currentUser, transferId)) {
                System.out.println("successfully rejected request!");
            }
        }
	}

	private void sendBucks() {

        User[] users = userService.getOtherUsers(currentUser);
        if (users == null) {
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("ID\t\t\tName");
        System.out.println("-------------------------------------------");

        int[] options = new int[users.length + 1];
        for (int i = 0; i < users.length; i++) {
            options[i] = Math.toIntExact(users[i].getId());

            System.out.println(users[i].getId() + "\t\t" + users[i].getUsername());
        }

        System.out.println("-------------------------------------------");
        System.out.println();

        int recipientUserId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):", options);
        if (recipientUserId == 0) {
            return;
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount (0 to cancel):", true);
        if (amount.doubleValue() == 0) {
            return;
        }

        TransferDTO transfer = new TransferDTO(Math.toIntExact(currentUser.getUser().getId()), recipientUserId, amount);
        if (transferService.sendBucks(currentUser, transfer)) {
            System.out.println("successfully sent funds!");
        }
	}

	private void requestBucks() {
        User[] users = userService.getOtherUsers(currentUser);
        if (users == null) {
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("ID\t\t\tName");
        System.out.println("-------------------------------------------");

        int[] options = new int[users.length + 1];
        for (int i = 0; i < users.length; i++) {
            options[i] = Math.toIntExact(users[i].getId());
            System.out.println(users[i].getId() + "\t\t" + users[i].getUsername());
        }

        System.out.println("-------------------------------------------");
        System.out.println();

        int targetUserId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel):", options);
        if (targetUserId == 0) {
            return;
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount (0 to cancel):", true);
        if (amount.doubleValue() == 0) {
            return;
        }

        TransferDTO transfer = new TransferDTO(targetUserId, Math.toIntExact(currentUser.getUser().getId()), amount);
        if (transferService.requestBucks(currentUser, transfer)) {
            System.out.println("successfully requested funds!");
        }
	}

}
