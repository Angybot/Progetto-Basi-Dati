//Applicazione GUI per la gestione degli utenti

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static java.awt.Color.*;
import java.sql.*;

public class GUIUtenti extends JFrame {

    // Configurazione Database
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/BaseDatiBus";
    static final String USER = "root";
    static final String PASS = "Angybot.2005";

    // Componenti Grafici
    private JTable table;
    private DefaultTableModel tableModel;

    public GUIUtenti() {
        // Setup della Finestra Principale
        setTitle("DataBus - Gestione Utenti");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Pannello Superiore (Titolo)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BLUE);
        JLabel titleLabel = new JLabel("Pannello di Controllo Utenti");
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(titleLabel);

        // Tabella Centrale (Visualizzazione Dati)
        String[] columnNames = {"ID", "Nome", "Cognome", "Email", "Via", "Civico", "CAP", "Città"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rende la tabella non modificabile
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25); // Righe un po' più alte
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);

        // Pannello Inferiore (Bottoni)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JButton btnAdd = new JButton("Nuovo Utente");
        JButton btnEdit = new JButton("Modifica Selezionato");
        JButton btnDelete = new JButton("Elimina Selezionato");
        JButton btnRefresh = new JButton("Aggiorna Tabella");

        // Stile bottoni
        styleButton(btnAdd, GREEN);
        styleButton(btnEdit, ORANGE);
        styleButton(btnDelete, RED);
        styleButton(btnRefresh, BLUE);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Assemblaggio layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Azioni dei Bottoni
        btnRefresh.addActionListener(e -> ricaricaDati());

        btnAdd.addActionListener(e -> mostraDialogoInserimento());

        btnDelete.addActionListener(e -> eliminaUtente());

        btnEdit.addActionListener(e -> modificaUtente());

        // Carica i dati all'avvio
        ricaricaDati();
    }

    // --- METODI PER IL DATABASE ---

    // Scarica i dati dal DB e riempie la tabella
    private void ricaricaDati() {
        tableModel.setRowCount(0); // Pulisce la tabella
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM UTENTE")) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("IDUtente"),
                        rs.getString("Nome"),
                        rs.getString("Cognome"),
                        rs.getString("Email"),
                        rs.getString("Via"),
                        rs.getString("NumeroCivico"),
                        rs.getString("CAP"),
                        rs.getString("Citta") // o Città
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore DB: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Apre una finestra per inserire dati
    private void mostraDialogoInserimento() {
        JTextField txtNome = new JTextField();
        JTextField txtCognome = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtVia = new JTextField();
        JTextField txtCivico = new JTextField();
        JTextField txtCap = new JTextField();
        JTextField txtCitta = new JTextField();

        Object[] message = {
                "Nome:", txtNome,
                "Cognome:", txtCognome,
                "Email:", txtEmail,
                "Via:", txtVia,
                "Numero Civico:", txtCivico,
                "CAP:", txtCap,
                "Città:", txtCitta
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nuovo Utente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "INSERT INTO UTENTE (Nome, Cognome, Email, Via, NumeroCivico, CAP, Citta) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, txtNome.getText());
                pstmt.setString(2, txtCognome.getText());
                pstmt.setString(3, txtEmail.getText());
                pstmt.setString(4, txtVia.getText());
                pstmt.setString(5, txtCivico.getText());
                pstmt.setString(6, txtCap.getText());
                pstmt.setString(7, txtCitta.getText());

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Utente inserito con successo!");
                ricaricaDati(); // Aggiorna la tabella
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore Inserimento: " + ex.getMessage());
            }
        }
    }

    // Elimina la riga selezionata
    private void eliminaUtente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un utente dalla tabella per eliminarlo.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0); // Prende l'ID
        int confirm = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare l'utente ID " + id + "?", "Conferma", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "DELETE FROM UTENTE WHERE IDUtente = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                ricaricaDati();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore Eliminazione: " + ex.getMessage());
            }
        }
    }

    // Modifica la riga selezionata
    private void modificaUtente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un utente per modificarlo.");
            return;
        }

        // Recupera i dati attuali dalla riga selezionata
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String oldNome = (String) tableModel.getValueAt(selectedRow, 1);
        String oldCognome = (String) tableModel.getValueAt(selectedRow, 2);
        String oldEmail = (String) tableModel.getValueAt(selectedRow, 3);
        String oldVia = (String) tableModel.getValueAt(selectedRow, 4);
        String oldCivico = (String) tableModel.getValueAt(selectedRow, 5);
        String oldCap = (String) tableModel.getValueAt(selectedRow, 6);
        String oldCitta = (String) tableModel.getValueAt(selectedRow, 7);

        // Pre-compila i campi con i vecchi valori
        JTextField txtNome = new JTextField(oldNome);
        JTextField txtCognome = new JTextField(oldCognome);
        JTextField txtEmail = new JTextField(oldEmail);
        JTextField txtVia = new JTextField(oldVia);
        JTextField txtCivico = new JTextField(oldCivico);
        JTextField txtCap = new JTextField(oldCap);
        JTextField txtCitta = new JTextField(oldCitta);

        Object[] message = {
                "Nome:", txtNome,
                "Cognome:", txtCognome,
                "Email:", txtEmail,
                "Via:", txtVia,
                "Numero Civico:", txtCivico,
                "CAP:", txtCap,
                "Città:", txtCitta
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Modifica Utente ID " + id, JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "UPDATE UTENTE SET Nome=?, Cognome=?, Email=?, Via=?, NumeroCivico=?, CAP=?, Citta=? WHERE IDUtente=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, txtNome.getText());
                pstmt.setString(2, txtCognome.getText());
                pstmt.setString(3, txtEmail.getText());
                pstmt.setString(4, txtVia.getText());
                pstmt.setString(5, txtCivico.getText());
                pstmt.setString(6, txtCap.getText());
                pstmt.setString(7, txtCitta.getText());
                pstmt.setInt(8, id);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Utente aggiornato!");
                ricaricaDati();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore Modifica: " + ex.getMessage());
            }
        }
    }

    // Per abbellire i bottoni
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    public static void main(String[] args) {
        // Avvia l'applicazione grafica
        SwingUtilities.invokeLater(() -> {
            new GUIUtenti().setVisible(true);
        });
    }
}