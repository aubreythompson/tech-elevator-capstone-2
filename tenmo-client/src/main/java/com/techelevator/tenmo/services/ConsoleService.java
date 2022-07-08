package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt, int min, int max) {
        System.out.print(prompt);
        while (true) {
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                if (selection < min || selection > max) {
                    throw new NumberFormatException();
                } else {
                    return selection;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            }
        }
    }

    public int promptForInt(String prompt, int[] options) {
        System.out.print(prompt);
        while (true) {
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                for (int option : options) {
                    if (selection == option) {
                        return selection;
                    }
                }
                throw new NumberFormatException();

            } catch (NumberFormatException e) {
                System.out.println("Please enter a number from the options listed.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt, boolean mustBePositive) {
        System.out.print(prompt);
        while (true) {
            try {
                BigDecimal input = new BigDecimal(scanner.nextLine());
                if (input.doubleValue() >= 0) {
                    return input;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a " + (mustBePositive ? "non-negative " : "") + "decimal number");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage(String message) {
        System.out.println("ERROR: " + message + " - Check the log for details.");
    }

}
