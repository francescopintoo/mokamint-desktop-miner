package it.univr.mokamintminer.core;

import java.io.File;
import java.time.LocalDateTime;

public class ReconnectingRemoteMiner implements Runnable {

    private final String endpoint;
    private final File plotFile;

    private volatile boolean running = true;
    private volatile boolean connected = false;

    public ReconnectingRemoteMiner(String endpoint, File plotFile) {
        this.endpoint = endpoint;
        this.plotFile = plotFile;
    }

    @Override
    public void run() {
        System.out.println("Starting ReconnectingRemoteMiner...");
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Plot file: " + plotFile.getAbsolutePath());

        if (!plotFile.exists()) {
            log("ERROR: plot file does not exist");
            return;
        }

        while (running) {
            try {

                if (!connected)
                    connect();
                mine();

            } catch (InterruptedException e) {
                log("Miner interrupted");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log("Error during mining: " + e.getMessage());
                connected = false;
                sleepBeforeReconnect();
            }
        }

        log("ReconnectingRemoteMiner stopped");
    }


    // I prossimi due metodi sono "estendibili" in modo da innescare il mining reale appena possibile

    /**
     *  Simula la connessione al nodo remoto.
     *  In futuro quì andrà il vero RemoteMiner.
     */
    private void connect() throws InterruptedException {
        log("Connecting to node...");
        Thread.sleep(1000);  // Simulazione handshake
        connected = true;
        log("Connected to node");
    }

    /**
     * Simula il mining usando il plot.
     * In futuro: richiesta lavoro, calcolo deadline, submit.
     */
    private void mine() throws InterruptedException {
        log("Mining using plot...");
        Thread.sleep(2000);  // Simulazione mining ciclo
        log("Mining cycle completed");
    }

    /**
     * Attesa prima del reconnect.
     */
    private void sleepBeforeReconnect() {
        try {
            log("Reconnecting in 3 seconds...");
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Ferma il miner in modo pulito.
     */
    public void stop() {
        log("Stopping miner...");
        running = false;
    }

    private void log(String message) {
        System.out.println("[" + LocalDateTime.now() + "] [MINER] " + message);
    }
}
