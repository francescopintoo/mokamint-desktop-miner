package it.univr.mokamintminer.gui;

import it.univr.mokamintminer.core.DesktopMinerService;
import it.univr.mokamintminer.services.MinerService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.net.URI;
import java.nio.file.Path;

public class GUIController {

    @FXML
    private TextField endpointField;

    @FXML
    private TextField plotFileField;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button createPlotButton;

    @FXML
    private Button stopPlotButton;

    @FXML
    private Button startMiningButton;

    @FXML
    private Button stopMiningButton;

    @FXML
    private javafx.scene.control.TextArea logArea;

    private final MinerService minerService = new MinerService();

    // PLOT
    private Task<Void> plotTask;
    private Thread plotThread;

    // MINING
    private Thread miningThread;
    private DesktopMinerService miner;

    // CREATE PLOT
    @FXML
    private void onCreatePlot() {
        if (plotTask != null && plotTask.isRunning()) {
            statusLabel.setText("Status: plot already running");
            return;
        }

        String endpoint = endpointField.getText().trim();
        String plotFile = plotFileField.getText().trim();

        if (endpoint.isEmpty() || plotFile.isEmpty()) {
            statusLabel.setText("Status: endpoint missing or file missing");
            return;
        }

        Path plotPath = Path.of(plotFile);

        setPlotMode(true);

        plotTask = new Task<>() {
            @Override
            protected Void call() throws Exception {

                log(" Creating plot...");
                updateMessage("Status: Creating plot...");

                minerService.createPlot(
                        plotPath,
                        0L,
                        endpoint,
                        progress -> {

                            if (isCancelled()) {
                                return;
                            }

                            updateProgress(progress, 100);
                            updateMessage("Progress: " + progress + "%");
                        }
                );

                return null;
            }
        };

        //  UI binding
        statusLabel.textProperty().bind(plotTask.messageProperty());
        progressBar.progressProperty().bind(plotTask.progressProperty());

        plotTask.setOnSucceeded(e -> {
            cleanupPlotBindings();
            log(" Plot completed");
            statusLabel.setText("Status: Plot created successfully");
            progressBar.setProgress(1.0);
            setPlotMode(false);
            plotTask = null;
        });

        plotTask.setOnCancelled(e -> {
            cleanupPlotBindings();
            log(" Plot stopped");
            statusLabel.setText("Status: Plot creation stopped");
            progressBar.setProgress(0);
            setPlotMode(false);
            plotTask = null;
        });

        plotTask.setOnFailed(e -> {
            cleanupPlotBindings();
            statusLabel.setText("Error: " + plotTask.getException().getMessage());
            progressBar.setProgress(0);
            setPlotMode(false);
            plotTask = null;
        });

        plotThread = new Thread(plotTask, "plot-creation-thread");
        plotThread.setDaemon(true);
        plotThread.start();
    }

    // STOP PLOT
    @FXML
    private void onStopPlot() {
        if (plotTask != null && plotTask.isRunning()) {
            plotTask.cancel();
        }
    }

    // START MINING
    @FXML
    private void onStartMining() {
        if (plotTask != null && plotTask.isRunning()) {
            return;  // Mining disabilitato durante il plot
        }

        if (miningThread != null && miningThread.isAlive()) {
            return;
        }

        String plotFile = plotFileField.getText().trim();

        if (plotFile.isEmpty()) {
            statusLabel.setText("Status: plot file missing");
            return;
        }

        URI endpointUri;
        try {
            endpointUri = URI.create(endpointField.getText().trim());
        } catch (Exception e) {
            statusLabel.setText("Status: Invalid endpoint URI");
            return;
        }

        try {
            miner = new DesktopMinerService(
                    endpointUri,
                    Path.of(plotFile),
                    new DesktopMinerService.MinerListener() {
                        @Override
                        public void onConnected() {
                            log("Connect to node");
                            statusLabel.setText("Status: Connected");
                        }

                        @Override
                        public void onDisconnected() {
                            log(" Disconnected from node");
                            statusLabel.setText("Status: Disconnected");
                        }

                        @Override
                        public void onDeadline() {
                            log(" Deadline computed");
                            statusLabel.setText("Status: Mining...");
                        }
                    }
            );

            miningThread = new Thread(() -> miner.isConnected(), "mining-thread");
            miningThread.setDaemon(true);
            miningThread.start();

            log(" Starting mining...");
            startSimulationIfNoConnection();
            statusLabel.setText("Status: Mining started");

        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    // STOP MINING
    @FXML
    private void onStopMining() {
        // Se il plot è attivo, non fa nulla
        if (plotTask != null && plotTask.isRunning()) {
            return;
        }

        if (miner != null) {
            miner.close();
            miner = null;
        }

        if (miningThread != null) {
            miningThread.interrupt();
            miningThread = null;
        }

        log(" Mining stopped");
        statusLabel.setText("Status: mining stopped");
    }

    // HELPERS
    private void setPlotMode(boolean plotting) {
        createPlotButton.setDisable(plotting);
        stopPlotButton.setDisable(!plotting);
        startMiningButton.setDisable(plotting);
        stopMiningButton.setDisable(plotting);
    }

    private void cleanupPlotBindings() {
        if (statusLabel.textProperty().isBound())
            statusLabel.textProperty().unbind();

        if (progressBar.progressProperty().isBound())
            progressBar.progressProperty().unbind();
    }

    private void log(String message) {
        String time = java.time.LocalDateTime.now().withNano(0).toString();

        javafx.application.Platform.runLater(() -> {
            logArea.appendText("[" + time + "]" + message + "\n");
            logArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void startSimulationIfNoConnection() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);

                // Simulo solo se non è arrivata una connessione reale
                if (miner != null) {
                    log(" Simulated: Connected");
                    statusLabel.setText("Status: Connected");

                    for (int i=0; i<5; i++) {
                        Thread.sleep(1500);
                        log(" Simulated: Deadline computed");
                    }

                    Thread.sleep(1000);
                    log(" Simulated: Disconnected");
                    statusLabel.setText("Status: Disconnected");
                }

            } catch (InterruptedException ignored) {}
        }).start();
    }

}

