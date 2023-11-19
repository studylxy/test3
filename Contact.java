package test10;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Contact {

    public static class DataBase {
        private Connection connection;

        public DataBase() {
            connection = null;
        }

        public void connectToDatabase(String url, String username, String password) {
            try {
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connected to database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void addContact(String name, String address, String phone) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO contacts (name, address, phone) VALUES (?, ?, ?)")) {
                statement.setString(1, name);
                statement.setString(2, address);
                statement.setString(3, phone);
                statement.executeUpdate();
                System.out.println("Contact added: " + name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void updateContact(String name, String address, String phone) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE contacts SET address = ?, phone = ? WHERE name = ?")) {
                statement.setString(1, address);
                statement.setString(2, phone);
                statement.setString(3, name);
                statement.executeUpdate();
                System.out.println("Contact updated: " + name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteContact(String name, String address, String phone) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM contacts WHERE name = ? AND address = ? AND phone = ?")) {
                statement.setString(1, name);
                statement.setString(2, address);
                statement.setString(3, phone);
                statement.executeUpdate();
                System.out.println("Contact deleted: " + name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public List<String> getContactList() {
            List<String> contacts = new ArrayList<>();
            try (Statement statement = connection.createStatement()) {
                String query = "SELECT * FROM contacts";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    contacts.add(name + "\t" + address + "\t" + phone);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return contacts;
        }

        public void closeConnection() {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Disconnected from database");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static class PersonalAddressBookClientController {
        private ClientUI client;
        private DataBase server;

        public PersonalAddressBookClientController(ClientUI client) {
            this.client = client;
            server = new DataBase();
        }

        public void connectToDatabase(String url, String username, String password) {
            server.connectToDatabase(url, username, password);
        }

        public void addContact(String name, String address, String phone) {
            server.addContact(name, address, phone);
        }

        public void updateContact(String name, String address, String phone) {
            server.updateContact(name, address, phone);
        }

        public void deleteContact(String name, String address, String phone) {
            server.deleteContact(name, address, phone);
        }

        public void refreshContactList() {
            List<String> contacts = server.getContactList();
            StringBuilder listBuilder = new StringBuilder();
            for (String contact : contacts) {
                listBuilder.append(contact).append("\n");
            }
            client.setContactList(listBuilder.toString());
        }

        public void closeConnection() {
            server.closeConnection();
        }
    }
}