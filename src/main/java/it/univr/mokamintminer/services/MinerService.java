package it.univr.mokamintminer.services;

import io.hotmoka.crypto.HashingAlgorithms;
import io.hotmoka.crypto.SignatureAlgorithms;
import io.hotmoka.crypto.api.HashingAlgorithm;
import io.hotmoka.crypto.api.SignatureAlgorithm;
import io.mokamint.nonce.Prologs;
import io.mokamint.nonce.api.Prolog;
import io.mokamint.plotter.Plots;

import java.nio.file.Path;
import java.security.KeyPair;

public class MinerService {

    public interface ProgressListener {
        void onProgress(int percent);
    }

    // TEST MODE
    // Plot piccolo per sviluppo, numero di nonce (poco per test: ca 1.25MB)
    private static final long TEST_PLOT_SIZE = 5;

    public void createPlot(Path plotPath,
                           long startNonce,
                           String endpoint,
                           ProgressListener listener
    ) throws Exception {

            // Algoritmi crypto
            SignatureAlgorithm signature = SignatureAlgorithms.ed25519();
            HashingAlgorithm hashing = HashingAlgorithms.sha256();

            // Chiavi locali del miner
            KeyPair blockKeys = signature.getKeyPair();
            KeyPair txKeys = signature.getKeyPair();

            // prolog valido
            Prolog prolog = Prologs.of(
                    "desktop-miner",
                    signature,
                    blockKeys.getPublic(),
                    signature,
                    txKeys.getPublic(),
                    new byte[0]
            );

            // dimensione plot documentata
            long plotSize = TEST_PLOT_SIZE;

            System.out.println("[PLOT] Creating plot:");
            System.out.println("[PLOT] Path: " + plotPath.toAbsolutePath());
            System.out.println("[PLOT] Nonces: " + plotSize);
            System.out.println("[PLOT] Estimated size: " + (plotSize * 262144 / (1024 * 1024)) + " MB");

            // creazione plot
            Plots.create(
                    plotPath,
                    prolog,
                    startNonce,
                    plotSize,
                    hashing,
                    progress -> {
                        if (listener != null)
                            listener.onProgress(progress);
                    }
            );

    }

}
