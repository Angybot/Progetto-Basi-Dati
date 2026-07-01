# DataBus - Sistema di Gestione Autotrasporti
DataBus è un progetto di ingegneria dei dati mirato alla progettazione e implementazione della base di dati operativa per un'azienda di autotrasporti e servizi di linea. Il sistema gestisce le anagrafiche degli utenti, la pianificazione delle tratte, l'assegnazione dei mezzi (bus), i turni del personale (autisti e controllori) e il ciclo di vita delle prenotazioni con relativi controlli di validità.

## Caratteristiche Principali
Pianificazione e Tratte: Gestione dei percorsi con calcolo ottimizzato dei posti disponibili.

**Logistica Mezzi e Personale:** Assegnazione flessibile dei bus alle tratte e gestione dei turni di guida degli autisti.

**Sicurezza e Controllo:** Registrazione dei controlli sui titoli di viaggio effettuati dai controllori con esito univoco per prenotazione.

**Interfaccia Grafica (GUI):** Applicazione desktop in Java Swing integrata via JDBC per la gestione CRUD completa dell'anagrafica utenti.

## Architettura del Database
**Schema Logico** (Relazionale)
UTENTE (IDUtente, Nome, Cognome, Via, CAP, Città, NumeroCivico, DataRegistrazione, Email)

TELEFONO (Numero, Utente)

PRENOTAZIONE (IDPrenotazione, DataPrenotazione, Utente, Tratta, Controllore, DataControllo, EsitoControllo)

CONTROLLORE (IDPersonale, Nome, Cognome)

TRATTA (IDTratta, Data, OrarioPartenza, OrarioArrivo, Partenza, Arrivo, NumeroPostiDisponibili, Bus)

BUS (CodiceBus, Azienda, Capacità)

GUIDARE (*Bus*, *Autista*, *Data*)

AUTISTA (IDPersonale, Nome, Cognome, NumeroPatente)

## Scelte di Ottimizzazione e Ristrutturazione
**Ridondanza Controllata:** Inserimento dell'attributo NumeroPostiDisponibili nell'entità TRATTA per abbattere il costo computazionale delle letture (da 4.300 a 100 accessi stimati/giorno).

**Eliminazione delle Gerarchie:** Accorpamento dell'entità genitore PERSONALE nelle entità figlie AUTISTA e CONTROLLORE.

**Risoluzione Attributi Composti/Multivalore:** Trasformazione di Telefono in un'entità correlata uno-a-molti e atomizzazione dell'attributo Indirizzo direttamente in UTENTE.

**Semplificazione Chiavi:** Introduzione dell'attributo IDTratta per eliminare la dipendenza dell'entità debole dal codice del bus.

## Stack Tecnico
DBMS: MySQL / MariaDB (SQL DDL, DML e Query strutturate)

GUI: Java 8+ (Java Swing, AWT)

**Connettività:** JDBC Driver

## Sviluppatori:
Andrea Zeno
Angelo Vito Saggese Tozzi
