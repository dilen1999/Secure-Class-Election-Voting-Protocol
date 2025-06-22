package model;

import crypto.CryptoUtils;
import java.security.PrivateKey;
import java.util.*;

public class Collector {
    private final PrivateKey privateKey;
    private final Set<String> receivedTokens = new HashSet<>();
    private final Map<String, Integer> voteCount = new HashMap<>();
    private final Map<String, String> voteHashMap = new HashMap<>();

    public Collector(PrivateKey privateKey) throws Exception {
        this.privateKey = privateKey;
        // Precompute known vote hashes
        voteHashMap.put(CryptoUtils.hash("A"), "A");
        voteHashMap.put(CryptoUtils.hash("B"), "B");
        voteHashMap.put(CryptoUtils.hash("C"), "C");
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

    public void showResults() {
        CryptoUtils.logToFile("\n--- Voting Summary ---");
        System.out.println("\n--- Voting Summary ---");
        CryptoUtils.logToFile("Total Voters: " + receivedTokens.size());
        System.out.println("Total Voters: " + receivedTokens.size());
        for (Map.Entry<String, Integer> entry : voteCount.entrySet()) {
            String voteHash = entry.getKey();
            int count = entry.getValue();
            String candidate = voteHashMap.getOrDefault(voteHash, "Unknown");
            String summary = "Candidate " + candidate + " → Count: " + count + " | Hashed Vote: " + voteHash;
            CryptoUtils.logToFile(summary);
            System.out.println(summary);
        }
    }
}
