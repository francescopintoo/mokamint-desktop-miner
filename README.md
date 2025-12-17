# Mokamint Desktop Miner

Progetto desktop JavaFX per la Laurea Triennale in Informatica.

## Descrizione

Questo progetto è una versione desktop del miner Mokamint desktop, ispirata all'applicazione Android Mokaminter.
L'obiettivo è riprodurre la struttura dell'applicazione Androind in ambiente desktop, implementando progressivamente la GUI e la logica di base del miner.
Attualmente il progetto permette di generare una chiave, creare un plot su disco e simulare l'invio del plot a un nodo Mokamint tramite endpoint. 

## Funzionalità implementate (al momento)

- GUI JavaFX con: 
  - campo per l'inserimento dell'endpoint del nodo Mokamint;
  - campo per la dimensione del plot (in MB);
  - campo per la plot key;
  - pulsante per la generazione di una nuova chiave;
  - pulsante per la creazione del plot;
  - label di stato che mostra l'avanzamento delle operazioni.
- Classe MinerService con: 
  - generateNewKeyPair(): genera una chiave cassuale utilizzando SecureRandom e la restituisce codificata in Base64;
  - createPlot(long plotSizeMB, String key, String endpoint):
    - crea un file .bin sul disco della dimensione specificata; 
    - usa la chiave per identificare il nome del plot; 
    - scrive dati casuali nel file per simulare un vero plot; 
    - simula l'invio del plot al nodo Mokamint indicato dall'endpoint; 
    - restituisce un messaggio di stato da mostrare nella GUI.

## Struttura del progetto

>     mokamint-desktop-miner/
>     pom.xml
>     README.md                                         # Documentazione
>     .gitignore                                        
> 
>       src/ 
>       main/
>         java/
>           it/univr/mokamintminer/
>             gui/
>               Main.java
>               GUIController.java
> 
>             services/
>               MinerService.java
> 
>             core/                                     # Vuoto per ora, logica miner futura
>             util/                                     # Vuoto per ora, utility future
> 
>         resources/
>           layout/
>             gui.fxml
>     
>       test/                                           # Test futuri
>         java/ 

## Come eseguire il progetto

1. Aprire il progetto con Intellij IDEA o Eclipse con supporto Maven.
2. Assicurarsi di avere Java 17 installato
3. Eseguire 'Main.java' come applicazione JavaFX.

## Note

- L'invio del plot al nodo Mokamint è solo simulato: non viene effettuata alcuna comunicazione di rete reale.
- L'endpoint viene comunque richiesto e utilizzato per mantenere coerenza con l'architettura dell'app Android.
- Il progetto è strutturato per permettere l'integrazione futura della comunicazione reale con il nodo Mokamint.
- Maven è utilizzato per gestire le dipendenze e l'avvio di JavaFX. 

