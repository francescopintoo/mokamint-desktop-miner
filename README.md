# Mokamint Desktop Miner
This project is a desktop application for the Mokamint blockchain that simulates and partially implements a real miner.

## Features
- Plot file creation (real implementation using Mokamint plotter)
- Mining simulation with GUI
- Integration with Mokamint miner APIs
- Reconnecting miner service (based on AbstractReconnectingMinerService)
- Graphical feedback for: 
  - Connection status
  - Deadline computation 
  - Mining activity

## Architecture
The application is structured into:

- 'GUIController': manage user interactions and UI updates
- 'DesktopMinerService': handles the connection to a Mokamint node 
- 'MinerService': manage plot creation
- JavaFX GUI

## Current Limitations
- A real Mokamint node is required to fully test mining 
- Without a running node, the miner: 
  - Attempts connection
  - Does not receive deadlines
  - Show no 'onConnected' events

## How to Run

### 1. Build the project
mvn clean install

### 2. Run the GUI
mvn javafx:run

### 3. Create a plot
- Insert a plot file path
- Click "Create plot"

### 4. Start mining 
- Insert node endpoint (e.g. ws://localhost:8033)
- Click "Start mining"

## Notes
This project demonstrates how a miner can be built on top of Mokamint APIs, using: 
- AbstractReconnectingMinerService
- Local plot-based mining
- Event-driven UI updates
The GUI replaces the textual feedback of the CLI miner with a visual interface. 
