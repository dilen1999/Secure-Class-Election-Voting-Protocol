package crypto;

import javax.crypto.Cipher;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class CryptoUtils {
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String result = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        logToFile("Encrypted Input (Base64): " + result);
        System.out.println("🔐 Encrypted Vote: " + result);
        return result;
    }

    public static String decrypt(String encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        String result = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)));
        logToFile("Decrypted Output: " + result);
        System.out.println("🔓 Decrypted Vote (hash): " + result);
        return result;
    }

    public static String hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String result = Base64.getEncoder().encodeToString(digest.digest(input.getBytes()));
        logToFile("Hashed Input (" + input + "): " + result);
        System.out.println("#️⃣ Hashed: " + result);
        return result;
    }

    public static void logToFile(String content) {
        try (FileWriter fw = new FileWriter("output.txt", true)) {
            fw.write(content + "\n");
//            System.out.println("📄 Log written to output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
