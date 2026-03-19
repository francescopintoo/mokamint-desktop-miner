package it.univr.mokamintminer.core;

import io.mokamint.miner.local.LocalMiners;
import io.mokamint.miner.service.AbstractReconnectingMinerService;
import io.mokamint.nonce.api.Deadline;
import io.mokamint.plotter.Plots;

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;

public class DesktopMinerService extends AbstractReconnectingMinerService {

    private final MinerListener listener;
    private int deadlines = 0;

    public interface MinerListener {
        void onConnected();
        void onDisconnected();
        void onDeadline();
    }

    public DesktopMinerService(URI endpoint, Path plotPath, MinerListener listener) throws Exception {
        super(
                Optional.of(
                        LocalMiners.of(
                                "Desktop Miner",
                                "Miner GUI",
                                (signature, publicKey) -> Optional.empty(),
                                Plots.load(plotPath)
                        )
                ),
                endpoint,
                30000,
                30000
        );

        this.listener = listener;
    }

    @Override
    protected void onConnected() {
        super.onConnected();

        if (listener != null)
            listener.onConnected();
    }

    @Override
    protected void onDisconnected() {
        super.onDisconnected();

        if (listener != null)
            listener.onDisconnected();
    }

    @Override
    protected void onDeadlineComputed(Deadline deadline) {
        super.onDeadlineComputed(deadline);

        if (listener != null)
            listener.onDeadline();

        deadlines++;
    }

}
