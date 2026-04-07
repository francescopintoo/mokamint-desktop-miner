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

    // Valore di default se l'utente non specifica nulla
    public static final long DEFAULT_PLOT_SIZE = 1000;

    public void createPlot(Path plotPath,
                           long startNonce,
                           long plotSize,
                           String endpoint,
                           ProgressListener listener
    ) throws Exception {

            // Algoritmi crypto
            SignatureAlgorithm signature = SignatureAlgorithms.ed25519();
            HashingAlgorithm hashing = HashingAlgorithms.shabal256();

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
