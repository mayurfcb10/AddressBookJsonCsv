package com.bridgelabz.addressbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	 private PreparedStatement contactDataStatement;
	 
	/* Method to setup Connection with database */
	private Connection getConnection() throws SQLException {
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
				ContactList.add(new ContactDetails(firstName, lastName, address, city, state, email, phoneNumber,
						zipCode, userId, typeId, contactType));
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
                    "join location on user_details.user_id = location.user_id\n" +
                    "join user_contact_type_link on user_details.user_id = user_contact_type_link.user_id\n" +
                    " join contact_type on user_contact_type_link.type_id = contact_type.type_id where user_details.first_name = ?;";
            contactDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.SQL_EXCEPTION);
        }
    }

}
