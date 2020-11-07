package com.bridgelabz.addressbook;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AddressBookService {
	private List<ContactDetails> contactList ;
	private static AddressBookDBService addressBookServiceDB;
	public AddressBookService() {
		addressBookServiceDB =AddressBookDBService.getInstance();
		this.contactList = new ArrayList<>();
	}

	public AddressBookService(List<ContactDetails> addressBookList) {
		this();
		this.contactList = new ArrayList<>(addressBookList);
	}

	
	public enum IOService {
		DB_IO, REST_IO
	}
	
	public int countEntries(IOService ioService) {
		if(ioService.equals(IOService.DB_IO))
		return contactList.size();
		return contactList.size();
	}


	public List<ContactDetails> readAddressBookData(IOService ioService) throws AddressBookException {
		if (ioService.equals(IOService.DB_IO)) {
			this.contactList = addressBookServiceDB.readData();
		}
		return this.contactList;
	}

	/*method to update contact information in Database*/
	public void updateContactInformation(String firstName, String lastName, IOService ioService) throws AddressBookException {
		int result = addressBookServiceDB.updateContactData(firstName, lastName);
		if (ioService.equals(IOService.DB_IO)) {
			if (result == 0) {
				try {
					throw new AddressBookException("unable to update the info", AddressBookException.ExceptionType.UPDATE_PROBLEM);
				} catch (AddressBookException e) {
					e.printStackTrace();
				}
			}
		}
		ContactDetails contact = this.getContactData(firstName);
		if (contact != null)
			contact.setLastName(lastName);
	}

	/*Method to get Contact using first name*/
	public ContactDetails getContactData(String firstName) {
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
	
	public void addContactDetails(ContactDetails contactData, IOService ioService) throws AddressBookException {
		if(ioService.equals(ioService.DB_IO)) {
			this.addContactDetails(contactData.getFirstName(),contactData.getLastName(),contactData.getAddress(),contactData.getCity(),contactData.getState(),contactData.getEmail(),contactData.getPhoneNumber(),
					contactData.getZip(), contactData.userId,contactData.typeId,contactData.contactType,contactData.startDate);
			System.out.println(contactList);
			
		}else
		{
			contactList.add(contactData);
		}
	}

	public void addContactDetails(String firstName, String lastName, String address, String city, String state,
								  String email, String phoneNumber, String zip, int userId, int  typeId, String contactType, LocalDate startDate) throws AddressBookException {
		this.contactList.add(addressBookServiceDB.addContactDetails(firstName,lastName,address,city,state,email,phoneNumber, zip, userId,typeId,contactType,startDate));
		System.out.println(contactList);
	}

	/* Add multiple contact to addressbook without thread */
	public void addContactsToAddressBook(List<ContactDetails> contactDataList) {
		contactDataList.forEach(contactDetailsData ->  {
				try {
					this.addContactDetails(contactDetailsData.getFirstName(),contactDetailsData.getLastName(),contactDetailsData.getAddress(),contactDetailsData.getCity(),contactDetailsData.getState(),contactDetailsData.getEmail(), contactDetailsData.getPhoneNumber(),
							contactDetailsData.getZip(),contactDetailsData.userId,contactDetailsData.typeId, contactDetailsData.contactType, contactDetailsData.startDate);
				} catch (AddressBookException e) {
					e.printStackTrace();
				}
		});
	}
	

	/* Add multiple contact to addressbook with thread */
	public void addContactsToAddressBookWithThreads(List<ContactDetails> contactDataList) {
		Map<Integer, Boolean> contactAdditionStatus = new HashMap<Integer, Boolean>();
		contactDataList.forEach(contactDetailsData ->  {
			Runnable task = () -> {
				contactAdditionStatus.put(contactDetailsData.hashCode(), false);
				System.out.println("Employee Being Added: " + Thread.currentThread().getName());
				try {
					this.addContactDetails(contactDetailsData.getFirstName(),contactDetailsData.getLastName(),contactDetailsData.getAddress(),contactDetailsData.getCity(),contactDetailsData.getState(),contactDetailsData.getEmail(), contactDetailsData.getPhoneNumber(),
							contactDetailsData.getZip(),contactDetailsData.userId,contactDetailsData.typeId, contactDetailsData.contactType, contactDetailsData.startDate);
				} catch (AddressBookException e) {
					e.printStackTrace();
				}
				contactAdditionStatus.put(contactDetailsData.hashCode(), true);
				System.out.println("Employee Added " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, contactDetailsData.getFirstName());
			thread.start();
		});
		while(contactAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(contactDataList);

	}

}

