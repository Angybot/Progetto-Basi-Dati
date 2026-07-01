# DataBus - Progetto di Base di Dati

Sviluppo della base di dati e dell'interfaccia di gestione operativa per un'azienda di autotrasporti e servizi di linea.

## Autori
* **Andrea Zeno**
* **Angelo Vito Saggese Tozzi** 

---

## Stack Tecnico
* **Database:** MySQL / MariaDB
* **Applicativo GUI:** Java (Java Swing)
* **Connessione DB:** JDBC Driver

---

## Specifiche di Progetto

### Schema Logico (Relazionale)
* **UTENTE** (IDUtente, Nome, Cognome, Via, CAP, Città, NumeroCivico, DataRegistrazione, Email)
* **TELEFONO** (Numero, Utente)
* **PRENOTAZIONE** (IDPrenotazione, DataPrenotazione, Utente, Tratta, Controllore\*, DataControllo\*, EsitoControllo\*)
* **CONTROLLORE** (IDPersonale, Nome, Cognome)
* **TRATTA** (IDTratta, Data, OrarioPartenza, OrarioArrivo, Partenza, Arrivo, NumeroPostiDisponibili, Bus)
* **BUS** (CodiceBus, Azienda, Capacità)
* **GUIDARE** (Bus, Autista, Data)
* **AUTISTA** (IDPersonale, Nome, Cognome, NumeroPatente)

### Vincoli non esprimibili nello Schema E-R
* **V1:** Impossibile effettuare una prenotazione se `NumeroPostiDisponibili` nella tratta è pari a zero.
* **V2:** La prenotazione deve essere effettuata cronologicamente prima della data/orario di partenza della tratta.

### Ottimizzazioni e Ridondanze
* L'attributo `NumeroPostiDisponibili` in **TRATTA** è stato reso persistente (ridondante) a seguito dell'analisi dei costi. Questa scelta riduce gli accessi globali stimati da 4.300 a 100 accessi/giorno per l'operazione ad alta frequenza di verifica dei posti.

---

## Funzionalità Implementate
* **DDL & DML:** Script completi di generazione schemi, vincoli di integrità referenziale (`ON DELETE CASCADE`) e popolamento dati.
* **Query SQL:** Implementazione di selezioni con logica booleana, funzioni aggregate, raggruppamenti (`GROUP BY` / `HAVING`), operazioni insiemistiche e divisione relazionale.
* **Interfaccia Grafica (GUI):** Applicativo desktop Java Swing integrato tramite JDBC per la gestione del ciclo di vita dei dati (operazioni CRUD) sulla tabella degli Utenti.
