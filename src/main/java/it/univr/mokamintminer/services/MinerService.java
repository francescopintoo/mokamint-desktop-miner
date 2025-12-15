package it.univr.mokamintminer.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

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
    public void createPlot(long plotSizeMB, String key) {
        System.out.println("Creating plot..." );
        System.out.println("Plot size (MB): " + plotSizeMB);
        System.out.println("Key: " + key);

        // File plot simulato
        String fileName = "plot_" + key.substring(0, Math.min(8, key.length())) + ".bin";
        File file = new File(fileName);

        // Numero di byte totali da scrivere
        long totalBytes = plotSizeMB * 1024 * 1024;

        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024 * 1024]; // buffer da 1MB
            long bytesWritten = 0;

            while (bytesWritten < totalBytes) {
                int bytesToWrite = (int) Math.min(buffer.length, totalBytes - bytesWritten);
                random.nextBytes(buffer);
                fos.write(buffer, 0, bytesToWrite);
                bytesWritten += bytesToWrite;
            }
        } catch (IOException e) {
            System.out.println("Error: unable to create plot file.");
            e.printStackTrace();
        }

        System.out.println("Status: plot created at " + file.getAbsolutePath() + " (" + plotSizeMB + " MB)");
    }
}
