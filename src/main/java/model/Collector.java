package model;

import crypto.CryptoUtils;
import java.security.PrivateKey;
import java.util.*;

public class Collector {
    private final PrivateKey privateKey;
    private final Set<String> receivedTokens = new HashSet<>();
    private final Map<String, Integer> voteCount = new HashMap<>();

    public Collector(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public boolean receiveVote(String encryptedVote, String token) throws Exception {
        CryptoUtils.logToFile("Receiving vote with token: " + token);

        if (receivedTokens.contains(token)) {
            CryptoUtils.logToFile("Duplicate vote detected. Vote rejected.");
            System.out.println(" Duplicate vote detected. Vote rejected.");
            return false;
        }

        CryptoUtils.logToFile("Decrypting vote...");
        String decryptedHash = CryptoUtils.decrypt(encryptedVote, privateKey);

        voteCount.put(decryptedHash, voteCount.getOrDefault(decryptedHash, 0) + 1);

        CryptoUtils.logToFile("Vote received and verified (hash): " + decryptedHash);
        System.out.println(" Vote received and verified (hash): " + decryptedHash);
        receivedTokens.add(token);
        return true;
    }

    public void showResults(Map<String, String> candidateHashMap) {
        CryptoUtils.logToFile("\n--- Voting Summary ---");
        System.out.println("\n--- Voting Summary ---");
        CryptoUtils.logToFile("Total Voters: " + receivedTokens.size());
        System.out.println("Total Voters: " + receivedTokens.size());
        
        String winner = null;
        int maxVotes = 0;
        
        for (Map.Entry<String, Integer> entry : voteCount.entrySet()) {
            String candidateName = candidateHashMap.getOrDefault(entry.getKey(), "Unknown");
            int count = entry.getValue();
            CryptoUtils.logToFile(candidateName + " → Count: " + count);
            System.out.println(candidateName + " → Count: " + count);

            if (count > maxVotes) {
                maxVotes = count;
                winner = candidateName;
            }
        }
        System.out.println("\n🎉 Winner is: " + winner + " with " + maxVotes + " votes!");
    }
}