package it.univr.mokamintminer.services;

public class MinerService {

    public String generateNewKey() {
        // implementare generazione chiave
        System.out.println("Generating new key...");
        return "TEMP_GENERATED_KEY";
    }

    public void createPlot(long plotSize, String key) {
        // implementare invio richiesta
        System.out.println("Creating plot..." );
        System.out.println("Plot size: " + plotSize);
        System.out.println("Key: " + key);
    }
}
