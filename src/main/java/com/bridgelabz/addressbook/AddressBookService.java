package com.bridgelabz.addressbook;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressBookService {
	private List<ContactDetails> contactList ;
	private static AddressBookDBService addressBookServiceDB;
	public AddressBookService() {
		addressBookServiceDB =AddressBookDBService.getInstance();
		this.contactList = new ArrayList<>();
	}

	enum IOService {
		DB_IO
	}

	public List<ContactDetails> readAddressBookData(IOService ioService) throws AddressBookException {
		if (ioService.equals(IOService.DB_IO)) {
			this.contactList = addressBookServiceDB.readData();
		}
		return this.contactList;
	}

	/*method to update contact information in Database*/
	public void updateContactInformation(String firstName, String lastName) throws AddressBookException {
		int result = addressBookServiceDB.updateContactData(firstName, lastName);
		if (result == 0) {
			try {
				throw new AddressBookException("unable to update the info", AddressBookException.ExceptionType.UPDATE_PROBLEM);
			} catch (AddressBookException e) {
				e.printStackTrace();
			}
		}
		ContactDetails contact = this.getContactData(firstName);
		if (contact != null)
			contact.setLastName(lastName);
	}

	/*Method to get Contact using first name*/
	private ContactDetails getContactData(String firstName) {
		return this.contactList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.getFirstName().equals(firstName))
				.findFirst()
				.orElse(null);
	}

	/*Method to check database in sync with Address book service*/
	public boolean checkAddressBookInSyncWithDB(String firstName) throws AddressBookException {
		List<ContactDetails> contactList = addressBookServiceDB.getContactData(firstName);
		return this.contactList.get(0).getFirstName().equals(getContactData(firstName).getFirstName());
	}

	public List<ContactDetails> readContactDataForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) throws AddressBookException {
		if(ioService.equals(IOService.DB_IO))
			this.contactList = addressBookServiceDB.readContactDataForDateRange(startDate,endDate);
		return this.contactList;
	}

	/*Method to read Contact by giving State as parameter*/
	public Map<String, String> readContactByStateOrCity(IOService ioService, String cityOrState) throws AddressBookException {
		if (ioService.equals(IOService.DB_IO ) && cityOrState.equals("state"))
			return  addressBookServiceDB.readContactByState();
		if (ioService.equals(IOService.DB_IO ) && cityOrState.equals("city"))
			return addressBookServiceDB.readContactByCity();
		return null;
	}

	public void addContactDetails(String firstName, String lastName, String address, String city, String state,
								  String email, String phoneNumber, String zip, int userId, int  typeId, String contactType, LocalDate startDate) throws AddressBookException {
		this.contactList.add(addressBookServiceDB.addContactDetails(firstName,lastName,address,city,state,email,phoneNumber, zip, userId,typeId,contactType,startDate));
		System.out.println(contactList);
	}


}

