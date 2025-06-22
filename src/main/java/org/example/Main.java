package org.example;

import crypto.CryptoUtils;
import model.Collector;
import model.Voter;
import service.AuthService;

import java.security.KeyPair;
import java.util.Map;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        // Display candidate list
        System.out.println("===== Candidates for Class Monitor =====");
        System.out.println("1. Alice");
        System.out.println("2. Bob");
        System.out.println("3. Charlie");
        System.out.println("========================================");


        // Load voter registry
        Map<String, String> voterRegistry = AuthService.getVoterRegistry();

        // Setup: Collector's RSA Key Pair
        KeyPair collectorKeyPair = CryptoUtils.generateRSAKeyPair();
        CryptoUtils.logToFile("Collector's RSA Key Pair generated.");

        // Collector setup
        Collector collector = new Collector(collectorKeyPair.getPrivate());

        
        // Map to store candidate name -> hashed vote
        Map<String, String> candidateHashMap = new HashMap<>();
        String[] candidates = {"Alice", "Bob", "Charlie"};

        for (String candidate : candidates) {
            candidateHashMap.put(CryptoUtils.hash(candidate), candidate);
        }

        // Simulate multiple voters (loop)
        boolean continueVoting = true;
        while (continueVoting) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            CryptoUtils.logToFile("User input - Username: " + username);

            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            CryptoUtils.logToFile("User input - Password: " + password);

            // Authenticate user
            if (!voterRegistry.containsKey(username) || !voterRegistry.get(username).equals(password)) {
                System.out.println(" Invalid username or password. Vote rejected.");
                CryptoUtils.logToFile(" Invalid login attempt: " + username);
                continue;
            }

            // Get valid vote input
            String vote;
            while (true) {
                System.out.print("Enter vote (A/B/C): ");
                vote = scanner.nextLine().trim().toUpperCase();
                if (validVotes.contains(vote)) break;
                System.out.println(" Invalid vote. Please enter A, B, or C.");
            }
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
        collector.showResults(candidateHashMap);
    }
}
