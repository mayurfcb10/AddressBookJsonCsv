package com.bridgelabz.addressbook;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

class AddressBookMain {
	public static Scanner sc = new Scanner(System.in);
	private static AddressBook addressBook = new AddressBook();
	public static Map<String, AddressBook> addressBookListMap = new HashMap<>();

	public void addAddressBook(String addressBookName) {
		AddressBookMain addBookMain = new AddressBookMain();
		boolean flag = true;

		while (flag) {

			System.out.println("1.Add Contact");
			System.out.println("2.Edit Contact");
			System.out.println("3.Delete");
			System.out.println("4.Exit");
			System.out.println("Enter Choice: ");

			int option = sc.nextInt();

			switch (option) {
			case 1 -> {
				System.out.println("Enter the number of Contacts to be added");
				int noOfContacts = sc.nextInt();
				for (int i = 0; i < noOfContacts; i++) {
					addressBook.addContactDetails();
				}
			}
			case 2 -> {
				System.out.println("Enter the Person First name to edit details: ");
				String personName = sc.next();
				boolean listEdited = addressBook.editContactDetails(personName);
				if (listEdited) {
					System.out.println("List Edited Successfully");
				} else {
					System.out.println("List Cannot be Edited");
				}
			}
			case 3 -> {
				System.out.println("Enter the Contact to be deleted:");
				String firstName = sc.next();
				boolean listDeleted = addressBook.deleteContact(firstName);
				if (listDeleted) {
					System.out.println("Deleted Contact from the List");
				} else {
					System.out.println("List Cannot be Deleted");
				}
			}
			case 4 -> flag = false;
			}
		}
		addressBookListMap.put(addressBookName, addressBook);
		System.out.println("Address Book Added Successfully");
	}

	private void searchPersonByState(String stateName) {
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			AddressBook value = entry.getValue();
			System.out.println("The Address Book: " + entry.getKey());
			value.getPersonNameByState(stateName);
		}
	}

	private void searchPersonByCity(String cityName) {
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			AddressBook value = entry.getValue();
			System.out.println("The Address Book: " + entry.getKey());
			value.getPersonNameByCity(cityName);
		}
	}

	private void viewPersonByStateUsingHashmap(String stateName) {
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			AddressBook value = entry.getValue();
			ArrayList<ContactDetails> contacts = value.personByState.entrySet().stream()
					.filter(findState -> findState.getKey().equals(stateName)).map(Map.Entry::getValue).findFirst()
					.orElse(null);
			assert contacts != null;
			for (ContactDetails contact : contacts) {
				System.out.println("First Name: " + contact.getFirstName() + " Last Name: " + contact.getLastName());
			}
		}
	}

	private void viewPersonByCityUsingHashMap(String cityName) {
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			AddressBook value = entry.getValue();
			ArrayList<ContactDetails> contacts = value.personByCity.entrySet().stream()
					.filter(findCity -> findCity.getKey().equals(cityName)).map(Map.Entry::getValue).findFirst()
					.orElse(null);
			assert contacts != null;
			for (ContactDetails contact : contacts) {
				System.out.println("First Name: " + contact.getFirstName() + " Last Name: " + contact.getLastName());
			}
		}
	}

	public void CountByState(String state) {
		int count = 0;
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			for (int i = 0; i < (entry.getValue()).contactList.size(); i++) {
				ContactDetails contact = entry.getValue().contactList.get(i);

				if (state.equals(contact.getState())) {
					count++;
				}

			}
		}
		System.out.println("Total Person Count in state " + state + ": " + count);
	}

	public void CountByCity(String city) {
		int countPersonInCity = 0;
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			for (int i = 0; i < (entry.getValue()).contactList.size(); i++) {
				ContactDetails d = (ContactDetails) entry.getValue().contactList.get(i);

				if (city.equals(d.getCity())) {
					countPersonInCity++;
				}

			}
		}
		System.out.println("Total number of people in this city " + city + ": " + countPersonInCity);
	}

	private void sortContactByName() {
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			AddressBook value = entry.getValue();
			List<ContactDetails> sortedList = value.contactList.stream()
					.sorted(Comparator.comparing(ContactDetails::getFirstName)).collect(Collectors.toList());

			for (ContactDetails contact : sortedList) {
				System.out.println("First Name: " + contact.getFirstName());
				System.out.println("Last Name: " + contact.getLastName());
			}
		}
	}

	private void sortContactByZipCode() {
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			AddressBook value = entry.getValue();
			List<ContactDetails> sortedList = value.contactList.stream()
					.sorted(Comparator.comparing(ContactDetails::getZip)).collect(Collectors.toList());

			for (ContactDetails contact : sortedList) {
				System.out.println("First Name: " + contact.getFirstName());
				System.out.println("Last Name: " + contact.getLastName());
			}
		}
	}

	private void sortContactByState() {
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			AddressBook value = entry.getValue();
			List<ContactDetails> sortedList = value.contactList.stream()
					.sorted(Comparator.comparing(ContactDetails::getState)).collect(Collectors.toList());

			for (ContactDetails contact : sortedList) {
				System.out.println("First Name: " + contact.getFirstName());
				System.out.println("Last Name: " + contact.getLastName());
			}
		}
	}

	private void sortContactByCity() {
		for (Map.Entry<String, AddressBook> entry : addressBookListMap.entrySet()) {
			AddressBook value = entry.getValue();
			List<ContactDetails> sortedList = value.contactList.stream()
					.sorted(Comparator.comparing(ContactDetails::getCity)).collect(Collectors.toList());

			for (ContactDetails contact : sortedList) {
				System.out.println("First Name: " + contact.getFirstName());
				System.out.println("Last Name: " + contact.getLastName());
			}
		}
	}

	public static void main(String[] args) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		System.out.println("Welcome to the Address Book Management System using Java Stream");
		AddressBookMain addressBookMain = new AddressBookMain();
		boolean flag = true;
		while (flag) {
			System.out.println("1.Add New Address Book");
			System.out.println("2.Find Duplicate Entry in Address Book");
			System.out.println("3.Search Contact from a city");
			System.out.println("4.Search Contact from a State");
			System.out.println("5.View contact By State Using State and Person HashMap");
			System.out.println("6.View Contact by city Using City and Person HashMap");
			System.out.println("7.Count Contact By State");
			System.out.println("8.Count Contact By City");
			System.out.println("9.Sort and Print in Alphabetical Order");
			System.out.println("10.Sort Contact By City");
			System.out.println("11.Sort Contact By State");
			System.out.println("12.Sort Contact By Zip Code");
			System.out.println("13.Write Data into the text file");
			System.out.println("14.Read data from text file to the console");
			System.out.println("15.Write Data into the CSV file");
			System.out.println("16.Read data from CSV file to the console");
			System.out.println("17.Write Data into the Json file");
			System.out.println("18.Read data from Json file to the console");
			System.out.println("19.Exit");
			
			String addressBookName = null;
			System.out.println("Enter choice: ");
			int option = sc.nextInt();
			switch (option) {
			case 1:
				System.out.println("Enter the Name of Address Book: ");
				addressBookName = sc.next();
				if (addressBookListMap.containsKey(addressBookName)) {
					System.out.println("The Address book Already Exists");
					break;
				} else {
					addressBookMain.addAddressBook(addressBookName);
					break;
				}

			case 2:
				for (Map.Entry<String, AddressBook> entry : addressBookMain.addressBookListMap.entrySet()) {
					AddressBook value = entry.getValue();
					System.out.println("Address Book Name: " + entry.getKey());
					value.checkDuplicate();
				}

			case 3:
				System.out.println("Enter Name of City: ");
				String cityName = sc.next();
				addressBookMain.searchPersonByCity(cityName);
				break;

			case 4:
				System.out.println("Enter Name of State: ");
				String stateName = sc.next();
				addressBookMain.searchPersonByState(stateName);
				break;

			case 5:
				System.out.println("Enter Name of State: ");
				String stateName1 = sc.next();
				addressBookMain.viewPersonByStateUsingHashmap(stateName1);
				break;

			case 6:
				System.out.println("Enter Name of City: ");
				String cityName1 = sc.next();
				addressBookMain.viewPersonByCityUsingHashMap(cityName1);
				break;

			case 7:
				System.out.println("Enter Name of State: ");
				String stateName2 = sc.next();
				addressBookMain.CountByState(stateName2);
				break;

			case 8:
				System.out.println("Enter Name of City: ");
				String cityName2 = sc.next();
				addressBookMain.CountByCity(cityName2);
				break;

			case 9:
				System.out.println("Sort");
				addressBookMain.sortContactByName();

			case 10:
				addressBookMain.sortContactByCity();
				break;

			case 11:
				addressBookMain.sortContactByState();
				break;

			case 12:
				addressBookMain.sortContactByZipCode();
				break;

			case 13:
				addressBook.writeData();
				break;

			case 14:
				addressBook.readData();
				break;

			case 15:
				try {
					addressBook.writeDataToCSV();
				} catch (IOException e) {
					System.out.println("Exception is - " + e);
				}
				
				break;

			case 16:
				try {
					addressBook.readDataUsingCSV();
				} catch (IOException e) {
					System.out.println("Exception is - " + e);
				}
				
				break;
				
			case 17:
				try {
					addressBook.writeDataInJSon();
				} catch (IOException e) {
					System.out.println("Exception is - " + e);
				}
				break;

			case 18:
				try {
					addressBook. readDataFromJson();
				} catch (IOException e) {
					System.out.println("Exception is - " + e);
				}
				break;

			case 19:
				flag = false;
				break;
			}
		}

	}

}
