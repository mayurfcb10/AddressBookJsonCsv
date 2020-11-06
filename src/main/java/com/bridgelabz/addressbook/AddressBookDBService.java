package com.bridgelabz.addressbook;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDBService {
    private PreparedStatement contactDataStatement;
    private static AddressBookDBService addressBookServiceDB;

    private AddressBookDBService() {
    }

    public static AddressBookDBService getInstance() {
        if (addressBookServiceDB == null) {
            addressBookServiceDB = new AddressBookDBService();
        }
        return addressBookServiceDB;
    }

    /* Method to setup Connection with database */
    private static Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/addressbookservice";
        String userName = "root";
        String password = "1994";
        Connection con;
        System.out.println("Connecting to database: " + jdbcURL);
        con = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection Successful: " + con);
        return con;
    }

    /* Method to read Contact from database */
    public List<ContactDetails> readData() throws AddressBookException {
        String sql = "select * from user_details join contact on user_details.user_id = contact.user_id "
                + "join location on user_details.user_id = location.user_id\n"
                + "join user_contact_type_link on user_details.user_id = user_contact_type_link.user_id\n"
                + " join contact_type on user_contact_type_link.type_id = contact_type.type_id;";
        return this.getAddressBookUsingDB(sql);
    }

    private List<ContactDetails> getAddressBookUsingDB(String sql) throws AddressBookException {
        List<ContactDetails> ContactList = null;
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ContactList = this.getEmployeepayrollData(resultSet);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
        }
        return ContactList;
    }

    private List<ContactDetails> getEmployeepayrollData(ResultSet resultSet) throws AddressBookException {
        List<ContactDetails> ContactList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                int typeId = resultSet.getInt("type_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zipCode = resultSet.getString("zip");
                String phoneNumber = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String contactType = resultSet.getString("type_of_contact");
                LocalDate startDate = resultSet.getDate("date_added").toLocalDate();
                ContactList.add(new ContactDetails(firstName, lastName, address, city, state, email, phoneNumber,
                        zipCode, userId, typeId, contactType, startDate));
            }
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.RETRIEVAL_PROBLEM);
        }
        return ContactList;
    }

    /*method to update contact details*/
    public int updateContactData(String first_name, String lastName) throws AddressBookException {
        return this.updateContactDataUsingPreparedStatement(first_name, lastName);
    }

    /*method to update contact details using prepared statement*/
    private int updateContactDataUsingPreparedStatement(String firstName, String lastName) throws AddressBookException {
        try (Connection connection = this.getConnection()) {
            String sql = "update user_details set last_name = ? where first_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, firstName);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.UPDATE_PROBLEM);
        }
    }

    /*Method to generate result for contact data*/
    public List<ContactDetails> getContactData(String firstName) throws AddressBookException {
        List<ContactDetails> contactList;
        if (this.contactDataStatement == null)
            this.prepareStatementForContactData();
        try {
            contactDataStatement.setString(1, firstName);
            ResultSet resultSet = contactDataStatement.executeQuery();
            contactList = this.getEmployeepayrollData(resultSet);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
        }
        return contactList;
    }

    /*Method to generate prepared statement for contact data*/
    private void prepareStatementForContactData() throws AddressBookException {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from user_details join contact on user_details.user_id = contact.user_id " +
                    "join location on user_details.user_id = location.user_id " +
                    "join user_contact_type_link on user_details.user_id = user_contact_type_link.user_id " +
                    " join contact_type on user_contact_type_link.type_id = contact_type.type_id where user_details.first_name = ?;";
            contactDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
        }
    }

    /*Method to get number of contacts added for given date range*/
    public List<ContactDetails> readContactDataForDateRange(LocalDate startDate, LocalDate endDate) throws AddressBookException {
        String sql = String.format("select * from user_details join contact on user_details.user_id = contact.user_id " +
                "join location on location.user_id = user_details.user_id\n" +
                "join user_contact_type_link on user_contact_type_link.user_id = user_details.user_id\n" +
                " join contact_type on contact_type.type_id = user_contact_type_link.type_id where user_details.date_added between '%s' and '%s'; ", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getAddressBookUsingDB(sql);
    }

    public Map<String, String> readContactByState() throws AddressBookException {
        String sql = String.format("SELECT first_name,state from user_details as ud join location as l on ud.user_id = l.user_id;");
        return readContactByCityOrState("first_name", "state",sql);
    }

    public Map<String, String> readContactByCity() throws AddressBookException {
        String sql = String.format("SELECT first_name,city from user_details as ud join location as l on ud.user_id = l.user_id;");
        return readContactByCityOrState("first_name", "city",sql);
    }

    private Map<String, String> readContactByCityOrState(String firstName, String cityOrState, String sql) throws AddressBookException {
        Map<String, String> CountCityOrStateMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String getResultSet1 = resultSet.getString(cityOrState);
                String getResultSet2 = resultSet.getString(firstName);
                CountCityOrStateMap.put(getResultSet1, getResultSet2);
            }
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
        }
        return CountCityOrStateMap;
    }

    public  ContactDetails addContactDetails(String firstName, String lastName, String address, String city,
                                                   String state, String email, String phoneNumber, String zip, int userId, int typeId, String contactType,
                                                   LocalDate startDate) throws AddressBookException {
        int contactId = -1;
        Connection connection = null;
        ContactDetails contactDetails = null;
        try {
            connection = AddressBookDBService.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.CONNECTION_PROBLEM);
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format(
                    "insert into user_details (user_id,first_name,last_name,date_added)" + "values ('%s', '%s', '%s', '%s')", userId,
                    firstName, lastName, Date.valueOf(startDate));
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    contactId = resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.INSERTION_PROBLEM);
        }

        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into location (user_id,address,city,state,zip) " + "VALUES (%d,'%s','%s','%s','%s')",
                    userId, address, city, state, zip);
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    contactId = resultSet.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            throw new AddressBookException("sql exception occured due to insertion in location",AddressBookException.ExceptionType.SQL_EXCEPTION);
        }

        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into contact (user_id,phone,email) " + "VALUES (%d,'%s','%s')",
                    userId, phoneNumber, email);
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    contactId = resultSet.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            throw new AddressBookException("Exception while insertion into Contact",AddressBookException.ExceptionType.SQL_EXCEPTION);
        }

        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into contact_type(type_id,type_of_contact) " + "VALUES (%d,'%s')",
                    typeId, contactType);
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    contactId = resultSet.getInt("type_id");
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            throw new AddressBookException("Exception while inserting the contact type ",AddressBookException.ExceptionType.SQL_EXCEPTION);
        }

        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into user_contact_type_link (user_id,type_id) " + "VALUES (%d,%d)",
                    userId, typeId);
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                contactDetails = new ContactDetails(firstName, lastName, address, city, state, email, phoneNumber, zip, userId, typeId, contactType, startDate);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            throw new AddressBookException("inserting into the contact type link",AddressBookException.ExceptionType.SQL_EXCEPTION);
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return contactDetails;
    }

}


















