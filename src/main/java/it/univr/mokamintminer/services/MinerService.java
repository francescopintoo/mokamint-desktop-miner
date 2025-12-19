package it.univr.mokamintminer.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

import it.univr.mokamintminer.core.ReconnectingRemoteMiner;

public class MinerService {

    private final SecureRandom random = new SecureRandom();

    /**
     * Genera una nuova chiave casuale codificata in Base64
     */
    public String generateNewKeyPair() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Crea un file "plot" sul disco della dimensione specificata (in MB) utilizzando la chiave fornita
     *
     * @param plotSizeMB dimensione del plot in MB
     * @param key chiave del plot
     */
    public String createPlot(long plotSizeMB, String key, String endpoint) {
        System.out.println("Creating plot..." );
        System.out.println("Plot size (MB): " + plotSizeMB);
        System.out.println("Plot Key: " + key);

        // File plot simulato
        String safeKey = key.replace("/", "_");
        String fileName = "plot_" + key.substring(0, Math.min(8, key.length())) + ".bin";
        File plotFile = new File(fileName);

        // Numero di byte totali da scrivere
        long totalBytes = plotSizeMB * 1024 * 1024;

        try (FileOutputStream fos = new FileOutputStream(plotFile)) {
            byte[] buffer = new byte[1024 * 1024]; // buffer da 1MB
            long bytesWritten = 0;

            while (bytesWritten < totalBytes) {
                int bytesToWrite = (int) Math.min(buffer.length, totalBytes - bytesWritten);
                random.nextBytes(buffer);
                fos.write(buffer, 0, bytesToWrite);
                bytesWritten += bytesToWrite;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error while creating plot";
        }

        System.out.println("Plot created at " + plotFile.getAbsolutePath());

        // Simulazione
        System.out.println("Sending plot to node " + endpoint + "...");
        simulateSend(plotFile, endpoint);
        System.out.println("Plot successfully sent");

        // Start miner
        ReconnectingRemoteMiner miner = new ReconnectingRemoteMiner(endpoint, plotFile);

        Thread minerThread = new Thread(miner);
        minerThread.setDaemon(true);
        minerThread.start();

        System.out.println("Remote miner started");

        return "Plot created and mining started";
    }

    private void simulateSend(File plotFile, String endpoint) {
        // simulazione: nessuna rete reale
        try {
            Thread.sleep(500); // solo per simulare un'attesa
        } catch (InterruptedException ignored) {
        }
    }
}
