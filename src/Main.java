import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Main {
    private JFrame frame;
    private Bank myBank;
    private JTabbedPane tabbedPane;

    // Main Color Pallete
    private final Color PRIMARY_BLUE = new Color(30, 50, 80);
    private final Color BG_LIGHT = new Color(245, 248, 250);
    private final Color ACCENT_BLUE = new Color(70, 130, 180);

    public Main() {
        myBank = loadBankData();
        frame = new JFrame("UET Bank System");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("UET Banking System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton btnSaveExit = new JButton("Save & Exit");
        btnSaveExit.setBackground(new Color(200, 50, 50));
        btnSaveExit.setForeground(Color.WHITE);
        btnSaveExit.setFocusPainted(false);
        btnSaveExit.addActionListener(e -> exitSystem());
        headerPanel.add(btnSaveExit, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);

        // TABS
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(PRIMARY_BLUE);

        tabbedPane.addTab("  Dashboard  ", createOverviewTab());
        tabbedPane.addTab("  Client Registration  ", createClientTab());
        tabbedPane.addTab("  Account Setup  ", createAccountTab());
        tabbedPane.addTab("  Deposit  ", createDepositTab());
        tabbedPane.addTab("  Withdrawal  ", createWithdrawTab());
        tabbedPane.addTab("  Client Removal  ", createRemoveClientTab());

        // Refreshing the overview tab
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                tabbedPane.setComponentAt(0, createOverviewTab());
            }
        });
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Auto Save if X is clicked
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitSystem();
            }
        });

        frame.setVisible(true);
    }

    // Tab Builders

    private JPanel createOverviewTab() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Stats Header
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(BG_LIGHT);

        statsPanel.add(createStatBox("Registered Clients", String.valueOf(myBank.getClientList().size())));
        statsPanel.add(createStatBox("Active Accounts", String.valueOf(myBank.getAccountList().size())));
        statsPanel.add(createStatBox("Total Bank Funds", "Rs. " + myBank.totalAmount()));

        panel.add(statsPanel, BorderLayout.NORTH);

        // Data
        String[] data = {"Client ID", "Name", "CNIC", "Phone", "Accounts", "Total Balance"};
        DefaultTableModel model = new DefaultTableModel(data, 0);

        for (Client client : myBank.getClientList()) {
            Person person = client.getPersonalDetails();
            model.addRow(new Object[]{
                    client.getId(), person.getName(), person.getCNIC(), person.getPhoneNumber(), client.getAccountList().size(), "Rs. " + client.totalAmount()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatBox(String title, String value) {
        JPanel box = new JPanel(new GridLayout(2, 1));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 2),
                new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lTitle.setForeground(ACCENT_BLUE);

        JLabel lValue = new JLabel(value);
        lValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lValue.setForeground(PRIMARY_BLUE);

        box.add(lTitle);
        box.add(lValue);
        return box;
    }

    private JPanel createClientTab() {
        JPanel panel = new JPanel(null);
        panel.setBackground(BG_LIGHT);

        JLabel title = new JLabel("Register a New Client");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(50, 30, 300, 30);
        panel.add(title);

        JLabel lName = new JLabel("Full Name:");
        lName.setBounds(50, 90, 100, 25);
        JTextField tName = new JTextField();
        tName.setBounds(160, 90, 200, 30);

        JLabel lCnic = new JLabel("CNIC:");
        lCnic.setBounds(50, 140, 100, 25);
        JTextField tCnic = new JTextField();
        tCnic.setBounds(160, 140, 200, 30);

        JLabel lPhone = new JLabel("Phone Number:");
        lPhone.setBounds(50, 190, 100, 25);
        JTextField tPhone = new JTextField();
        tPhone.setBounds(160, 190, 200, 30);

        JButton btnSave = new JButton("Save the Client Info");
        btnSave.setBounds(160, 250, 200, 40);
        btnSave.setBackground(ACCENT_BLUE);
        btnSave.setForeground(Color.WHITE);

        btnSave.addActionListener(e -> {
            if (!tName.getText().isEmpty() && !tCnic.getText().isEmpty() && !tPhone.getText().isEmpty()) {
                Person person = new Person(tName.getText(), tCnic.getText(), tPhone.getText());
                Client client = myBank.addClient(person);
                saveBankData();
                JOptionPane.showMessageDialog(frame, "Success!\nClient Added. Client ID: " + client.getId());
                tName.setText(""); tCnic.setText(""); tPhone.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(lName); panel.add(tName);
        panel.add(lCnic); panel.add(tCnic);
        panel.add(lPhone); panel.add(tPhone);
        panel.add(btnSave);
        return panel;
    }

    private JPanel createRemoveClientTab() {
        JPanel panel = new JPanel(null);
        panel.setBackground(BG_LIGHT);

        JLabel title = new JLabel("Remove Existing Client");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(50, 30, 350, 30);
        panel.add(title);

        JLabel lCnic = new JLabel("Client CNIC:");
        lCnic.setBounds(50, 90, 100, 25);
        JTextField tCnic = new JTextField();
        tCnic.setBounds(160, 90, 200, 30);

        JButton btnRemove = new JButton("Delete Client");
        btnRemove.setBounds(160, 150, 200, 40);

        // Red Color for Deletion method
        btnRemove.setBackground(new Color(200, 50, 50));
        btnRemove.setForeground(Color.WHITE);

        btnRemove.addActionListener(e -> {
            String cnic = tCnic.getText().trim();
            if (cnic.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a CNIC.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Find the client from existing Clients
            Client client = myBank.searchCustomerDetail(cnic);

            if (client == null) {
                JOptionPane.showMessageDialog(frame, "No client exists with this CNIC: " + cnic, "Not Found", JOptionPane.ERROR_MESSAGE);
            } else {
                // Confirming Deletion
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to  delete " + client.getPersonalDetails().getName() + " and ALL their accounts?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Calling the removeClient method
                    boolean success = myBank.removeClient(client.getId());
                    if (success) {
                        saveBankData(); // Instantly update the text file
                        JOptionPane.showMessageDialog(frame, "Client and all associated accounts successfully deleted.");
                        tCnic.setText("");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to remove client.", "System Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        panel.add(lCnic);
        panel.add(tCnic);
        panel.add(btnRemove);
        return panel;
    }

    private JPanel createAccountTab() {
        JPanel panel = new JPanel(null);
        panel.setBackground(BG_LIGHT);

        JLabel title = new JLabel("Open a New Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(50, 30, 300, 30);
        panel.add(title);

        JLabel lCnic = new JLabel("Client CNIC:");
        lCnic.setBounds(50, 90, 100, 25);
        JTextField tCnic = new JTextField();
        tCnic.setBounds(160, 90, 200, 30);

        JLabel lType = new JLabel("Account Type:");
        lType.setBounds(50, 140, 100, 25);
        JComboBox<String> comboType = new JComboBox<>(new String[]{"Savings Account", "Current Account"});
        comboType.setBounds(160, 140, 200, 30);

        JLabel lAmount = new JLabel("Initial Deposit:");
        lAmount.setBounds(50, 190, 100, 25);
        JTextField tAmount = new JTextField();
        tAmount.setBounds(160, 190, 200, 30);

        JLabel lExtra = new JLabel("Rate / Over Draft Limit:");
        lExtra.setBounds(10, 240, 160, 25);
        JTextField tExtra = new JTextField();
        tExtra.setBounds(160, 240, 200, 30);

        JButton btnOpen = new JButton("Open Account");
        btnOpen.setBounds(160, 300, 200, 40);
        btnOpen.setBackground(ACCENT_BLUE);
        btnOpen.setForeground(Color.WHITE);

        btnOpen.addActionListener(e -> {
            Client client = myBank.searchCustomerDetail(tCnic.getText());
            if (client == null) {
                JOptionPane.showMessageDialog(frame, "Client not found!"); return;
            }
            try {
                float amount = Float.parseFloat(tAmount.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(frame, "Initial deposit must be greater than zero.");
                    return;
                }
                float extra = Float.parseFloat(tExtra.getText());
                if (comboType.getSelectedIndex() == 0) {
                    SavingsAccount save = new SavingsAccount(amount, client, extra);
                    myBank.addAccount(save, client);
                    JOptionPane.showMessageDialog(frame, "Success!\nSavings Account Opened: " + save.getNumber());
                } else {
                    CurrentAccount current = new CurrentAccount(amount, client, extra);
                    myBank.addAccount(current, client);
                    JOptionPane.showMessageDialog(frame, "Success!\nCurrent Account Opened: " + current.getNumber());
                }
                saveBankData();
                tCnic.setText(""); tAmount.setText(""); tExtra.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Number Format!");
            }
        });
        panel.add(lCnic); panel.add(tCnic);
        panel.add(lType); panel.add(comboType);
        panel.add(lAmount); panel.add(tAmount);
        panel.add(lExtra); panel.add(tExtra);
        panel.add(btnOpen);
        return panel;
    }

    private JPanel createDepositTab() {
        JPanel panel = new JPanel(null);
        panel.setBackground(BG_LIGHT);

        JLabel title = new JLabel("Depositing the Funds");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(50, 30, 300, 30);
        panel.add(title);

        JLabel lAcc = new JLabel("Account No:");
        lAcc.setBounds(50, 90, 100, 25);
        JTextField tAcc = new JTextField();
        tAcc.setBounds(160, 90, 200, 30);

        JLabel lAmount = new JLabel("Amount:");
        lAmount.setBounds(50, 140, 100, 25);
        JTextField tAmount = new JTextField();
        tAmount.setBounds(160, 140, 200, 30);

        JButton btnDeposit = new JButton("Process the Deposit");
        btnDeposit.setBounds(160, 200, 200, 40);
        btnDeposit.setBackground(new Color(40, 160, 80));
        btnDeposit.setForeground(Color.WHITE);

        btnDeposit.addActionListener(e -> {
            Account acc = myBank.searchAccount(tAcc.getText());
            if (acc == null) {
                JOptionPane.showMessageDialog(frame, "Account not found!");
                return;
            }
            try {
                float amount = Float.parseFloat(tAmount.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(frame, "Amount must be greater than zero.");
                    return;
                }
                acc.deposit(amount);
                JOptionPane.showMessageDialog(frame, "Deposited! New Balance: Rs. " + acc.getAmount());
                saveBankData();
                tAcc.setText(""); tAmount.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Amount Format!");
            }
        });

        panel.add(lAcc); panel.add(tAcc);
        panel.add(lAmount); panel.add(tAmount);
        panel.add(btnDeposit);
        return panel;
    }

    private JPanel createWithdrawTab() {
        JPanel panel = new JPanel(null);
        panel.setBackground(BG_LIGHT);

        JLabel title = new JLabel("Withdraw the Funds");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(50, 30, 300, 30);
        panel.add(title);

        JLabel lAcc = new JLabel("Account Number:");
        lAcc.setBounds(50, 90, 100, 25);
        JTextField tAcc = new JTextField();
        tAcc.setBounds(160, 90, 200, 30);

        JLabel lAmount = new JLabel("Amount:");
        lAmount.setBounds(50, 140, 100, 25);
        JTextField tAmount = new JTextField();
        tAmount.setBounds(160, 140, 200, 30);

        JButton btnWithdraw = new JButton("Process Withdrawal");
        btnWithdraw.setBounds(160, 200, 200, 40);
        btnWithdraw.setBackground(new Color(200, 100, 40));
        btnWithdraw.setForeground(Color.WHITE);

        btnWithdraw.addActionListener(e -> {
            Account acc = myBank.searchAccount(tAcc.getText());
            if (acc == null) {
                JOptionPane.showMessageDialog(frame, "Account not found!");
                return;
            }
            try {
                float amount = Float.parseFloat(tAmount.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(frame, "Amount must be greater than zero.");
                    return;
                }
                float oldBalance = acc.getAmount();
                acc.withdraw(amount);
                if (oldBalance != acc.getAmount()) {
                    JOptionPane.showMessageDialog(frame, "Withdrawn! New Balance: Rs. " + acc.getAmount());
                    saveBankData();
                    tAcc.setText(""); tAmount.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Transaction Failed. Account limit exceeded.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Amount Format!");
            }
        });

        panel.add(lAcc); panel.add(tAcc);
        panel.add(lAmount); panel.add(tAmount);
        panel.add(btnWithdraw);
        return panel;
    }

    private void exitSystem() {
        saveBankData();
        JOptionPane.showMessageDialog(frame, "Data saved. Shutting Down.....");
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }

    private Bank loadBankData() {
        Bank loadedBank = new Bank("UET Student Bank");
        int maxClientCount = 0;
        int maxAccountCount = 999;

        Account.count = 1000;
        Client.count = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader("BankData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the string at the |
                String[] parts = line.split("\\|");

                if (parts[0].equals("CLIENT")) {
                    Person person = new Person(parts[2], parts[3], parts[4]);
                    Client client = loadedBank.addClient(person);
                    client.setId(parts[1]);

                    // Track max ID for static counter
                    int idNum = Integer.parseInt(parts[1].replace("CLIENT", ""));
                    if (idNum > maxClientCount) maxClientCount = idNum;

                } else if (parts[0].equals("SAVINGS")) {
                    Client owner = loadedBank.searchCustomerDetail(parts[3]);
                    if (owner != null) {
                        SavingsAccount sa = new SavingsAccount(Float.parseFloat(parts[2]), owner, Float.parseFloat(parts[4]));
                        sa.setNumber(parts[1]);
                        loadedBank.addAccount(sa, owner);

                        int accNum = Integer.parseInt(parts[1].replace("ACCOUNT", ""));
                        if (accNum > maxAccountCount) maxAccountCount = accNum;
                    }

                } else if (parts[0].equals("CURRENT")) {
                    Client owner = loadedBank.searchCustomerDetail(parts[3]);
                    if (owner != null) {
                        CurrentAccount ca = new CurrentAccount(Float.parseFloat(parts[2]), owner, Float.parseFloat(parts[4]));
                        ca.setNumber(parts[1]); // Override auto-generated ID
                        loadedBank.addAccount(ca, owner);

                        int accNum = Integer.parseInt(parts[1].replace("ACCOUNT", ""));
                        if (accNum > maxAccountCount) maxAccountCount = accNum;
                    }
                }
            }

            Client.count = maxClientCount + 1;
            Account.count = maxAccountCount + 1;

            System.out.println("Previous Bank Data Loaded Successfully.");

        } catch (FileNotFoundException e) {
            System.out.println("No previous save exists. Generating new files.....");
        } catch (Exception e) {
            System.out.println("Error reading text file. Restarting....");
        }
        return loadedBank;
    }

    private void saveBankData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("BankData.txt"))) {
            // Clients
            for (Client client : myBank.getClientList()) {
                Person person = client.getPersonalDetails();
                writer.println("CLIENT|" + client.getId() + "|" + person.getName() + "|" + person.getCNIC() + "|" + person.getPhoneNumber());
            }

            // Accounts
            for (Account a : myBank.getAccountList()) {
                if (a instanceof SavingsAccount) {
                    SavingsAccount save = (SavingsAccount) a;
                    writer.println("SAVINGS|" + save.getNumber() + "|" + save.getAmount() + "|" + save.getAccountHolder().getPersonalDetails().getCNIC() + "|" + save.getInterestRate());
                } else if (a instanceof CurrentAccount) {
                    CurrentAccount current = (CurrentAccount) a;
                    writer.println("CURRENT|" + current.getNumber() + "|" + current.getAmount() + "|" + current.getAccountHolder().getPersonalDetails().getCNIC() + "|" + current.getOverdraftLimit());
                }
            }
            System.out.println("Data successfully saved to BankData.txt");
        } catch (IOException e) {
            System.out.println("Failed to save text data. " + e.getMessage());
        }
    }
}
