package org.example;

import crypto.CryptoUtils;
import model.Collector;
import model.Voter;

import java.security.KeyPair;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Setup: Collector's RSA Key Pair
        KeyPair collectorKeyPair = CryptoUtils.generateRSAKeyPair();
        CryptoUtils.logToFile("Collector's RSA Key Pair generated.");

        // Collector setup
        Collector collector = new Collector(collectorKeyPair.getPrivate());

        // Simulate multiple voters (loop)
        boolean continueVoting = true;
        while (continueVoting) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            CryptoUtils.logToFile("User input - Username: " + username);

            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            CryptoUtils.logToFile("User input - Password: " + password);

            System.out.print("Enter vote: ");
            String vote = scanner.nextLine();
            CryptoUtils.logToFile("User input - Vote: " + vote);

            Voter voter = new Voter(username, password, vote);
            String token = voter.getToken();
            String encryptedVote = voter.encryptVote(collectorKeyPair.getPublic());

            collector.receiveVote(encryptedVote, token);

            System.out.print("Add another voter? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            continueVoting = response.equals("yes");
        }

        // Show final results
        collector.showResults();
    }
}
