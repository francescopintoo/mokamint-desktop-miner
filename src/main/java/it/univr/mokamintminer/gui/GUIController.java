package it.univr.mokamintminer.gui;

import it.univr.mokamintminer.services.MinerService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GUIController {

    @FXML
    private TextField endpointField;

    @FXML
    private TextField plotSizeField;

    @FXML
    private TextField plotKeyField;

    @FXML
    private Label statusLabel;

    private final MinerService minerService = new MinerService();

    @FXML
    private void onGenerateKey() {
        statusLabel.setText("Status: generating new key...");

        String newKey = minerService.generateNewKeyPair();
        plotKeyField.setText(newKey);

        statusLabel.setText("Status: new key generated");
    }

    @FXML
    private void onCreatePlot() {
        String endpoint = endpointField.getText().trim();
        String plotSizeText = plotSizeField.getText().trim();
        String plotKey = plotKeyField.getText().trim();

        if (endpoint.isEmpty() || plotSizeText.isEmpty() || plotKey.isEmpty()) {
            statusLabel.setText("Status: please fill in all fields");
            return;
        }

        long plotSize;
        try {
            plotSize = Long.parseLong(plotSizeText);
        } catch (NumberFormatException e) {
            statusLabel.setText("Status: plot size must be a number");
            return;
        }
        statusLabel.setText("Status: creating plot...");

        // ancora da creare: la logica verrà aggiunta
        minerService.createPlot(plotSize, plotKey);

        statusLabel.setText("Status: plot created");
    }
}
