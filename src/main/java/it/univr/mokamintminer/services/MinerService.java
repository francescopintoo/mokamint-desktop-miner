package it.univr.mokamintminer.services;

import java.security.SecureRandom;
import java.util.Base64;

public class MinerService {

    private final SecureRandom random = new SecureRandom();

    public String generateNewKeyPair() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public void createPlot(long plotSize, String key) {
        // implementare invio richiesta
        System.out.println("Creating plot..." );
        System.out.println("Plot size: " + plotSize);
        System.out.println("Key: " + key);
    }
}
