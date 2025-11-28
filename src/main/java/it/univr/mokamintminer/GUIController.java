package it.univr.mokamintminer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GUIController {

    @FXML
    private TextField endpointField;

    @FXML
    private Label statusLabel;

    private MinerService minerService = new MinerService();

    @FXML
    private void onGenerateKey() {
        statusLabel.setText("Generazione chiave...");
        // per ora è solo una funzione finta
        minerService.generateKeyPair();
        statusLabel.setText("Chiave generata");
    }

    @FXML
    private void onCreateMiner() {
        String endpoint = endpointField.getText().trim();

        if (endpoint.isEmpty()) {
            statusLabel.setText("Errore: inserire un endpoint");
            return;
        }

        statusLabel.setText("Creazione miner in corso...");
        // anche questa ancora da creare
        minerService.createMiner(endpoint);
        statusLabel.setText("Miner creato");
    }
}
