package model;

import crypto.CryptoUtils;
import java.security.PublicKey;

public class Voter {
    private final String username;
    private final String password;
    private final String vote;

    public Voter(String username, String password, String vote) {
        this.username = username;
        this.password = password;
        this.vote = vote;
    }

    public String getToken() throws Exception {
        String raw = username + password;
        CryptoUtils.logToFile("Generating token using: " + raw);
        return CryptoUtils.hash(raw);
    }

    public String getHashedVote() throws Exception {
        CryptoUtils.logToFile("Hashing vote choice: " + vote);
        return CryptoUtils.hash(vote);
    }

    public String encryptVote(PublicKey collectorPublicKey) throws Exception {
        String hashedVote = getHashedVote();
        CryptoUtils.logToFile("Encrypting hashed vote...");
        return CryptoUtils.encrypt(hashedVote, collectorPublicKey);
    }

    public String getRawVote() {
        return vote;
    }
}