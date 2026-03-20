# Mokamint Desktop Miner

A desktop application for the Mokamint blockchain that implements plot file creation and mining activity with a 
graphical interface.

The project was developed as part of university thesis at the University of Verona, with the goal of replacing the 
textual feedback of the existing CLI miner with a visual JavaFX interface, built on top of the official Mokamint APIs.

---

## Features
- Plot file creation using the official Mokamint plotter ('io-mokamint-plotter')
- Configurable plot size (number of nonces) directly from the GUI
- Real-time progress bar during plot creation, with cancellation support
- Mining session management via 'AbstractReconnectingMinerService'
- Live graphical feedback for: 
  - Connection status to a Mokamint node
  - Deadline computation counter
  - Mining activity log with timestamps
- Graceful fallback simulation when no node is reachable (see Design Choises)

---

## Architecture

The application is structured into three main layers:

- **`Main`** - JavaFX entry point, loads the FXML layout and launches the stage
- **`GUIController`** - handles all user interactions and UI updates; coordinates between the plot and mining services
- **`DesktopMinerService`** - extends 'AbstractReconnectingMinerService'; wraps the Mokamint miner APIs and exposes 
                             connection and deadline events via a 'MinerListener' interface
- **`MinerService`** - encapsulates plot creation logic using 'Plots.create()' with real cryptographic keys and a valid 
                       prolog

---

### Prerequisites

- Java 21
- Maven 3.8+ 

## How to run

### 1. Clone the repository

```bash
  git clone https://github.com/francescopintoo/mokamint-desktop-miner.git
  cd mokamint-desktop-miner
```

### 2. Build the project

```bash 
  mvn clean install
```

### 3. Run the GUI

```bash
  mvn javafx:run
```

### 4. Create a plot

- Enter the node endpoint (e.g. `ws://localhost:8033`)
- Enter a path for the plot file (e.g. `plot.bin`)
- Enter the number of nonces (leave blank for default: 1000, ~250 MB)
- Click **Create plot** and wait for the progress bar to complete
- Click **Stop plot** to cancel at any time

### 5. Start mining 

- Make sure a plot file has been created
- Enter the node endpoint
- Click **Start mining** 
- The log area will show connection events and computed deadlines in real time
- The deadlines counter updates after each computation
- Click **Stop mining** to end the session

---

## Design Choises and Known Limitations

## Plot creation - fully real

Plot creation is fully implemented using the official Mokamint APIs. Specifically: 

-`SignatureAlgorithms.ed25519()` and `HashingAlgorithms.sha256()` are used for cryptographic setup
- A valid `Prolog` is constructed with locally generated key pairs
- `Plots.create()` is called directly, producing a real plot file on disk
- The plot size is configurable from the GUI (deafault: 100 nonces, ~250 MB)

The plot size is currently fixed at `1000` nonces (approximately 250 MB) for development purposes. This is controlled by 
the `TEST_PLOT_SIZE` constant in `MinerService.java`.

## Mining - real integration with simulation fallback

The mining layer is built on `AbstractReconnectingMinerService` and uses `LocalMiners.of()` with a real loaded plot 
file. The integration is real: if a Mokamint node is available at the given endpoint, the miner will connect, receive 
deadlines, and fire the corresponding callbacks. 

However, during development, activating a local Mokamint node consistently proved infeasible due to the 
following issues: 
  
- `ApplocationNotFoundException` when launching the node's empty application
- WebSocket handshake errors during connection attempts
- Module configuration and Maven execution issues with the node components

These are known infrastructures-level issues tied to the early-stage nature of the Mokamint project, and are not caused
by errors in this application's code. 

As a result, when no node connection is established within 3 seconds of starting mining, tha application activates an 
event-driven simulation that replicates the expected node behaviour: a connected event, a sequence of deadline 
computations, and a disconnected event. If a real node becomes available and `onConnected()` fires, the simulation is 
immediately suppressed via an `AtomicBoolean` flag. 

This approach mirrors standard industry practice for testing event-driven system in the absence of external 
infrastructure. 

---

## Project Structure

```
mokamint-desktop-miner/
├── src/
│   └── main/
│       ├── java/
│       │   └── it/univr/mokamintminer/
│       │       ├── core/
│       │       │   └── DesktopMinerService.java
│       │       ├── gui/
│       │       │   ├── GUIController.java
│       │       │   └── Main.java
│       │       └── services/
│       │           └── MinerService.java
│       └── resources/
│           └── layout/
│               └── gui.fxml
├── pom.xml
└── README.md
```

---

## Dependencies

| Artifact                       | Version | Purpose                            |
|--------------------------------|---------|------------------------------------|
| `io-mokamint-plotter`          | 1.6.1   | Plot file creation                 |
| `io-mokamint-plotter-api`      | 1.6.1   | Plotter API                        |
| `io-mokamint-miner-local`      | 1.6.1   | Local miner implementation         |
| `io-mokamint-miner-service`    | 1.6.1   | `AbstravtReconnectingMinerService` |
| `io-mokamint-miner-remote-api` | 1.6.1   | Remote miner API                   |
| `io-mokamint-node-api`         | 1.6.1   | Node API                           |
| `io-mokamint-node-remote`      | 1.6.1   | Remote node connection             |
| `javafx-controls`              | 21      | GUI controls                       |
| `javafx-fxml`                  | 21      | FXML layout loading                |
| `toml4j`                       | 0.7.3   | TOML configuration support         |

