//Per vedere come funzionano i driver JDBC e come fare la gestione del ciclo di vita dei dati (CRUD)

import java.sql.*;
import java.util.Scanner;

    public class GestioneUtente {

        // Parametri di connessione
        static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/BaseDatiBus";
        static final String USER = "root";
        static final String PASS = "Angybot.2005";

        public static void main(String[] args) {
            Connection conn = null;
            Scanner scanner = new Scanner(System.in);

            try {
                // Caricamento del Driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Apertura Connessione
                System.out.println("Connessione al database in corso...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Connessione riuscita!\n");

                while (true) {
                    System.out.println("\n--- MENU DATABUS ---");
                    System.out.println("1. Visualizza tutti gli Utenti (SELECT)");
                    System.out.println("2. Inserisci nuovo Utente (INSERT)");
                    System.out.println("3. Aggiorna dati Utente (UPDATE)");
                    System.out.println("4. Elimina Utente (DELETE)");
                    System.out.println("0. Esci");
                    System.out.print("Scegli un'opzione: ");
                    // Controllo input per evitare crash se si inserisce una lettera
                    if (scanner.hasNextInt()) {
                        int scelta = scanner.nextInt();
                        scanner.nextLine(); // Consuma invio

                        if (scelta == 0) break;

                        switch (scelta) {
                            case 1:
                                visualizzaUtenti(conn);
                                break;
                            case 2:
                                inserisciUtente(conn, scanner);
                                break;
                            case 3:
                                aggiornaUtente(conn, scanner);
                                break;
                            case 4:
                                eliminaUtente(conn, scanner);
                                break;
                            default:
                                System.out.println("Scelta non valida.");
                        }
                    } else {
                        System.out.println("Per favore inserisci un numero.");
                        scanner.nextLine(); // Pulisce il buffer
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Chiusura Connessione
                try {
                    if (conn != null) conn.close();
                    scanner.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        // OPERAZIONE 1: SELECT
        private static void visualizzaUtenti(Connection conn) throws SQLException {
            Statement stmt = conn.createStatement();
            String sql = "SELECT IDUtente, Nome, Cognome, Email, DataRegistrazione, Via, CAP, Città, NumeroCivico FROM UTENTE";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n--- ELENCO UTENTI ---");
            while (rs.next()) {
                int id = rs.getInt("IDUtente");
                String nome = rs.getString("Nome");
                String cognome = rs.getString("Cognome");
                String email = rs.getString("Email");
                Date dataRegistrazione = rs.getDate("DataRegistrazione");
                String via = rs.getString("Via");
                String cap = rs.getString("CAP");
                String citta = rs.getString("Città");
                String civico = rs.getString("NumeroCivico");
                System.out.println(id + " | " + nome + " " + cognome + " | " + email + " | registrato in data " + dataRegistrazione + " | Via " + via + ", " + civico + ", " + citta + " CAP: " + cap);
            }
            rs.close();
            stmt.close();
        }

        // OPERAZIONE 2: INSERT (Ora chiede tutti i dati)
        private static void inserisciUtente(Connection conn, Scanner scanner) throws SQLException {
            System.out.println("\n--- INSERIMENTO NUOVO UTENTE ---");

            // L'ID non lo chiediamo perché è AUTO_INCREMENT

            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("Cognome: ");
            String cognome = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Via: ");
            String via = scanner.nextLine();

            System.out.print("Numero Civico: ");
            String civico = scanner.nextLine();

            System.out.print("CAP: ");
            String cap = scanner.nextLine();

            System.out.print("Città: ");
            String citta = scanner.nextLine();

            // Query parametrica con tutti i campi
            String sql = "INSERT INTO UTENTE (Nome, Cognome, Email, Via, NumeroCivico, CAP, Citta) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);
            pstmt.setString(3, email);
            pstmt.setString(4, via);
            pstmt.setString(5, civico);
            pstmt.setString(6, cap);
            pstmt.setString(7, citta);

            int rows = pstmt.executeUpdate();
            System.out.println("Utente inserito con successo! Righe modificate: " + rows);
            pstmt.close();
        }

        // OPERAZIONE 3: UPDATE (Con menù di scelta)
        private static void aggiornaUtente(Connection conn, Scanner scanner) throws SQLException {
            System.out.println("\n--- MODIFICA UTENTE ---");
            System.out.print("Inserisci ID dell'utente da modificare: ");
            if (!scanner.hasNextInt()) {
                System.out.println("ID non valido.");
                scanner.nextLine();
                return;
            }
            int id = scanner.nextInt();
            scanner.nextLine(); // Consuma invio

            System.out.println("Cosa vuoi modificare?");
            System.out.println("1. Nome");
            System.out.println("2. Cognome");
            System.out.println("3. Email");
            System.out.println("4. Via");
            System.out.println("5. Numero Civico");
            System.out.println("6. CAP");
            System.out.println("7. Città");
            System.out.println("0. Annulla");
            System.out.print("Scelta: ");

            int sceltaCampo = scanner.nextInt();
            scanner.nextLine(); // Consuma invio

            String colonnaDB = "";
            String nomeCampo = "";

            switch (sceltaCampo) {
                case 1: colonnaDB = "Nome"; nomeCampo = "Nuovo Nome"; break;
                case 2: colonnaDB = "Cognome"; nomeCampo = "Nuovo Cognome"; break;
                case 3: colonnaDB = "Email"; nomeCampo = "Nuova Email"; break;
                case 4: colonnaDB = "Via"; nomeCampo = "Nuova Via"; break;
                case 5: colonnaDB = "NumeroCivico"; nomeCampo = "Nuovo Civico"; break;
                case 6: colonnaDB = "CAP"; nomeCampo = "Nuovo CAP"; break;
                case 7: colonnaDB = "Citta"; nomeCampo = "Nuova Città"; break; // Verifica se nel DB è 'Citta' o 'Città'
                case 0: System.out.println("Modifica annullata."); return;
                default: System.out.println("Scelta non valida."); return;
            }

            System.out.print(nomeCampo + ": ");
            String nuovoValore = scanner.nextLine();

            // Costruiamo la query dinamicamente in base alla scelta
            String sql = "UPDATE UTENTE SET " + colonnaDB + " = ? WHERE IDUtente = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nuovoValore);
            pstmt.setInt(2, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Dato aggiornato con successo!");
            else System.out.println("Utente non trovato con ID: " + id);
            pstmt.close();
        }

        // OPERAZIONE 4: DELETE
        private static void eliminaUtente(Connection conn, Scanner scanner) throws SQLException {
            System.out.print("Inserisci ID dell'utente da eliminare: ");
            int id = scanner.nextInt();

            String sql = "DELETE FROM UTENTE WHERE IDUtente = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Utente eliminato.");
            else System.out.println("Utente non trovato.");
            pstmt.close();
        }
    }

