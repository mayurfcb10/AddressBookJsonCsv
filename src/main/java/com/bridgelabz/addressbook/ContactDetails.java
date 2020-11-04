package com.bridgelabz.addressbook;

import java.time.LocalDate;

import com.opencsv.bean.CsvBindByName;

public class ContactDetails {

	@CsvBindByName(column = "FirstName")
	private String firstName;
	@CsvBindByName(column = "LastName")
	private String lastName;
	@CsvBindByName(column = "Address")
	private String address;
	@CsvBindByName(column = "City")
	private String city;
	@CsvBindByName(column = "State")
	private String state;
	@CsvBindByName(column = "Email")
	private String email;
	@CsvBindByName(column = "ZipName")
	private String zip;
	@CsvBindByName(column = "PhoneNumber")
	private String phoneNumber;
	public int userId;
	public int typeId;
	public String contactType;
	private LocalDate startDate;

	public ContactDetails(String firstName, String lastName, String address, String city, String state, String email,
			String phoneNumber, String zip) {
		setFirstName(firstName);
		setLastName(lastName);
		setAddress(address);
		setCity(city);
		setState(state);
		setEmail(email);
		setPhoneNumber(phoneNumber);
		setZip(zip);

	}

	public ContactDetails(String firstName, String lastName, String address, String city, String state, String email,
			String phoneNumber, String zip, int userId, int typeId, String contactType) {
		this(firstName, lastName, address, city, state, email, phoneNumber, zip);
		this.userId = userId;
		this.typeId = typeId;
		this.contactType = contactType;
	}
	
	public ContactDetails(String firstName, String lastName, String address, String city, String state, String email,
			String phoneNumber, String zip, int userId, int typeId, String contactType, LocalDate startDate) {
		this(firstName, lastName, address, city, state, email, phoneNumber, zip,userId, typeId, contactType);
		this.startDate = startDate;
		
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public ContactDetails() {

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZip() {
		return zip;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((contactType == null) ? 0 : contactType.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + typeId;
		result = prime * result + userId;
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactDetails other = (ContactDetails) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (contactType == null) {
			if (other.contactType != null)
				return false;
		} else if (!contactType.equals(other.contactType))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (typeId != other.typeId)
			return false;
		if (userId != other.userId)
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ContactDetails [firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + ", city="
				+ city + ", state=" + state + ", email=" + email + ", zip=" + zip + ", phoneNumber=" + phoneNumber
				+ "]";
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
