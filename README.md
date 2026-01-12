# Mokamint Desktop Miner

Progetto desktop JavaFX per la Laurea Triennale in Informatica.

## Descrizione

Questo progetto è una versione desktop del miner Mokamint desktop, ispirata all'applicazione Android Mokaminter.
L'obiettivo è riprodurre la struttura dell'applicazione Androind in ambiente desktop, implementando progressivamente la GUI e la logica di base del miner.
Attualmente il progetto permette di generare un plot su disco, inviarlo (simulato) a un nodo Mokamint tramite endpoint e avviare/arrestare il mining, con gestione corretta dello stato della GUI. 

## Funzionalità implementate (al momento)

- GUI JavaFX con: 
  - campo per l'inserimento dell'endpoint del nodo Mokamint;
  - campo per il nome/directory del plot '.bin';
  - pulsante per creare il plot;
  - pulsante per fermare la creazione del plot;
  - pulsante per avviare il mining;
  - pulsante per fermare il mining;
  - progress bar e label di stato che mostrano l'avanzamento delle operazioni.
- Classe 'MinerService' con:
  - 'createPlot(Path plotPath, long startNonce, String endpoint)':
    - crea un file '.bin' sul disco del plot specificato; 
    - contatta l'endpoint prima di generare il plot per ottenere informazioni di mining; 
    - scrive dati casuali nel file per simulare un vero plot; 
    - permette l'interruzione della creazione del plot ('stop plot'); 
    - aggiorna lo stato della GUI in tempo reale.
- Classe 'ReconnectingRemoteMiner': gestisce il mining usando il plot generato, con start/stop indipendenti dal plot. 

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
>             core/                                     # Logica miner 
>               ReconnectingRemoteMiner.java
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
2. Assicurarsi di avere Java 21 installato
3. Eseguire 'Main.java' come applicazione JavaFX.

## Note

- L'invio del plot al nodo Mokamint è solo simulato, ma l'endpoint viene comunque contattato per coerenza con l'app Android.
- I bottoni vengono disabilitati durante la creazione del plot o l'esecuzione del mining per evitare conflitti.
- Il progetto è pronto per future estensioni, come comunicazioni reali con il nodo Mokamint.
- Maven gestisce le dipendeze e l'avvio di JavaFX. 

