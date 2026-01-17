package it.univr.mokamintminer.gui;

import it.univr.mokamintminer.core.ReconnectingRemoteMiner;
import it.univr.mokamintminer.services.MinerService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.File;
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

    private final MinerService minerService = new MinerService();

    // PLOT
    private Task<Void> plotTask;
    private Thread plotThread;

    // MINING
    private Thread miningThread;
    private ReconnectingRemoteMiner miner;

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

                updateMessage("Creating plot...");

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

            statusLabel.setText("Plot created successfully");
            progressBar.setProgress(1.0);
            setPlotMode(false);
            plotTask = null;
        });

        plotTask.setOnCancelled(e -> {
            cleanupPlotBindings();
            statusLabel.setText("Plot creation stopped");
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

        String endpoint = endpointField.getText().trim();
        String plotFile = plotFileField.getText().trim();

        if (endpoint.isEmpty() || plotFile.isEmpty()) {
            statusLabel.setText("Status: endpoint or plot file missing");
            return;
        }

        miner = new ReconnectingRemoteMiner(endpoint, new File(plotFile));

        miningThread = new Thread(() -> {
            miner.run();
        }, "mining-thread");

        miningThread.setDaemon(true);
        miningThread.start();

        statusLabel.setText("Status: mining started");
    }

    // STOP MINING
    @FXML
    private void onStopMining() {

        // Se il plot è attivo, non fa nulla
        if (plotTask != null && plotTask.isRunning()) {
            return;
        }

        if (miner != null) {
            miner.stop();
            miner = null;
        }

        if (miningThread != null) {
            miningThread.interrupt();
            miningThread = null;
        }

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
}

