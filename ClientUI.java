package test10;


import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextArea contactListArea;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private Contact.PersonalAddressBookClientController controller;

    public ClientUI() {
        super("个人通讯录系统");

        // Create UI components
        nameField = createTextField(20);
        addressField = createTextField(20);
        phoneField = createTextField(20);
        contactListArea = new JTextArea(10, 30);
        addButton = createButton("添加");
        updateButton = createButton("更新");
        deleteButton = createButton("删除");

        // Add event listeners
        addButton.addActionListener(e -> {
            controller.addContact(nameField.getText(), addressField.getText(), phoneField.getText());
            clearFields();
            refreshContactList();
            showMessage("添加成功!");
        });

        updateButton.addActionListener(e -> {
            controller.updateContact(nameField.getText(), addressField.getText(), phoneField.getText());
            clearFields();
            refreshContactList();
            showMessage("更新成功!");
        });

        deleteButton.addActionListener(e -> {
            controller.deleteContact(nameField.getText(), addressField.getText(), phoneField.getText());
            clearFields();
            refreshContactList();
            showMessage("删除成功!");
        });

        // Build the user interface
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        addComponent(inputPanel, new JLabel("姓名:"), gbc, 0, 0);
        addComponent(inputPanel, nameField, gbc, 1, 0);
        addComponent(inputPanel, new JLabel("地址:"), gbc, 0, 1);
        addComponent(inputPanel, addressField, gbc, 1, 1);
        addComponent(inputPanel, new JLabel("电话:"), gbc, 0, 2);
        addComponent(inputPanel, phoneField, gbc, 1, 2);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(contactListArea), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set window properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack(); // Adjusts the window size to fit the components
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 400)); // Set a minimum size to prevent the window from becoming too small

        // Set font size
        setFontSize(16);
    }

    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(textField.getFont().deriveFont(16f));
        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(16f));
        return button;
    }

    private void addComponent(JPanel panel, JComponent component, GridBagConstraints gbc, int gridx, int gridy) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(component, gbc);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void setController(Contact.PersonalAddressBookClientController controller) {
        this.controller = controller;
    }

    public void setContactList(String contactList) {
        contactListArea.setText(contactList);
    }

    public void clearFields() {
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
    }

    public void refreshContactList() {
        controller.refreshContactList();
    }

    public void setFontSize(int size) {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, size);
        nameField.setFont(font);
        addressField.setFont(font);
        phoneField.setFont(font);
        contactListArea.setFont(font);
        addButton.setFont(font);
        updateButton.setFont(font);
        deleteButton.setFont(font);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientUI client = new ClientUI();

            Contact.PersonalAddressBookClientController controller = new Contact.PersonalAddressBookClientController(client);
            client.setController(controller);

            client.setVisible(true);
            controller.connectToDatabase("jdbc:mysql://localhost:3306/personalCall", "root", "123456");
            controller.refreshContactList();
        });
    }
}