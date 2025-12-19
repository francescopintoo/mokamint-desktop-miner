package it.univr.mokamintminer.core;

import java.io.File;

public class ReconnectingRemoteMiner implements Runnable {
    private final String endpoint;
    private final File plotFile;
    private volatile boolean running = true;

    public ReconnectingRemoteMiner(String endpoint, File plotFile) {
        this.endpoint = endpoint;
        this.plotFile = plotFile;
    }

    @Override
    public void run() {
        System.out.println("Starting ReconnectingRemoteMiner...");
        System.out.println("Connecting to node " + endpoint);
        System.out.println("Using plot file: " + plotFile.getAbsolutePath());

        while (running) {
            try {
                System.out.println("Mining in progress...");
                Thread.sleep(2000);  // Simulazione mining
            } catch (InterruptedException e) {
                System.out.println("Miner interrupted, retrying connection...");
            }
        }
    }

    public void stop() {
        running = false;
        System.out.println("Miner stopped");
    }
}
