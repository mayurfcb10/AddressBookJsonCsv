package com.bridgelabz.addressbook;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class AddressBook {
	public static Scanner sc = new Scanner(System.in);
	public ArrayList<ContactDetails> contactList;
	public HashMap<String, ArrayList<ContactDetails>> personByState;
	public HashMap<String, ArrayList<ContactDetails>> personByCity;
	
	public AddressBook() {
		personByCity = new HashMap<String, ArrayList<ContactDetails>>();
		personByState = new HashMap<String, ArrayList<ContactDetails>>();
		contactList = new ArrayList<>();
	}

	public void writeData() {
		StringBuffer empBuffer = new StringBuffer();
		contactList.forEach(contact -> {
			String contactData = contact.toString().concat("\n");
			empBuffer.append(contactData);
		});
		try {
			Files.write(Paths.get("addressBook-file.txt"), empBuffer.toString().getBytes());

		} catch (IOException e) {

		}
	}

	public void readData() {
		try {
			Files.lines(new File("addressBook-file.txt").toPath()).map(line -> line.trim())
					.forEach(line -> System.out.println(line));

		} catch (IOException e) {

		}
	}

	public ArrayList<ContactDetails> addContactDetails() {
		System.out.println("Enter the contact details:");
		System.out.println("Enter first Name:");
		String firstName = sc.next();
		checkDuplicate();
		System.out.println("Enter the last Name");
		String lastName = sc.next();
		System.out.println("Enter the Address");
		String address = sc.next();
		System.out.println("Enter the City");
		String city = sc.next();
		System.out.println("Enter the State");
		String state = sc.next();
		System.out.println("Enter the Email");
		String email = sc.next();
		System.out.println("Enter the PhoneNumber");
		String phoneNumber = sc.next();
		System.out.println("Enter the Zip");
		String zip = sc.next();
		ContactDetails contactDetails = new ContactDetails(firstName, lastName, address, city, state, email,
				phoneNumber, zip);
		contactList.add(contactDetails);
		if (!personByState.containsKey(state)) {
			personByState.put(state, new ArrayList<ContactDetails>());
		}
		personByState.get(state).add(contactDetails);

		if (!personByCity.containsKey(city)) {
			personByCity.put(city, new ArrayList<ContactDetails>());
		}
		personByCity.get(city).add(contactDetails);

		return contactList;
	}

	public boolean editContactDetails(String Name) {
		int flag = 0;
		for (ContactDetails contact : contactList) {
			if (contact.getFirstName().equals(Name)) {
				System.out.println("Enter the detail which needs to be updated:");
				System.out.println("1 : First Name of the contact to be edited");
				System.out.println("2 : Last Name of the contact to be edited");
				System.out.println("3 : Address of the contact to be edited");
				System.out.println("4 : City of the contact to be edited");
				System.out.println("5 : State of the contact to be edited");
				System.out.println("6 : Email of the contact to be edited");
				System.out.println("7 : Phone Number of the contact to be edited");
				System.out.println("8 : Zip of the contact to be edited");

				System.out.println("Select the index for the contact detail you want to edit ");

				int choice = sc.nextInt();
				switch (choice) {
				case 1 -> {
					System.out.println("Enter First Name: ");
					String firstName = sc.next();
					contact.setFirstName(firstName);
					break;
				}
				case 2 -> {
					System.out.println("Enter last name: ");
					String lastName = sc.next();
					contact.setLastName(lastName);
					break;
				}
				case 3 -> {
					System.out.println("Enter Address: ");
					String address = sc.next();
					contact.setAddress(address);
					break;
				}
				case 4 -> {
					System.out.println("Enter City: ");
					String city = sc.next();
					contact.setCity(city);
					break;
				}
				case 5 -> {
					System.out.println("Enter State: ");
					String state = sc.next();
					contact.setState(state);
					break;
				}
				case 6 -> {
					System.out.println("Enter Email: ");
					String email = sc.next();
					contact.setZip(email);
					break;
				}
				case 7 -> {
					System.out.println("Enter Phone Number:");
					String phoneNumber = sc.next();
					contact.setPhoneNumber(phoneNumber);
					break;
				}
				case 8 -> {
					System.out.println("Enter Zip Code: ");
					String zip = sc.next();
					contact.setZip(zip);
					break;
				}
				}

				flag = 1;
				break;
			}
		}
		return flag == 1;
	}

	public boolean deleteContact(String name) {
		int flag = 0;
		for (ContactDetails contact : contactList) {
			if (contact.getFirstName().equals(name)) {
				contactList.remove(contact);
				flag = 1;
				break;
			}
		}
		return flag == 1;
	}

	public void checkDuplicate() {
		Set<String> ContactSet = new HashSet<>();
		Set<ContactDetails> filterSet = contactList.stream().filter(contact -> !ContactSet.add(contact.getFirstName()))
				.collect(Collectors.toSet());

		for (ContactDetails contact : filterSet) {
			System.out.println("The Duplicate Contact is: " + contact.getFirstName() + " " + contact.getLastName());
		}
	}

	public void getPersonNameByState(String State) {
		List<ContactDetails> list = contactList.stream().filter(contactName -> contactName.getState().equals(State))
				.collect(Collectors.toList());
		for (ContactDetails contact : list) {
			System.out.println("First Name: " + contact.getFirstName());
			System.out.println("Last Name: " + contact.getLastName());
		}

	}

	public void getPersonNameByCity(String cityName) {
		List<ContactDetails> list = contactList.stream().filter(contactName -> contactName.getCity().equals(cityName))
				.collect(Collectors.toList());
		for (ContactDetails contact : list) {
			System.out.println("First Name: " + contact.getFirstName());
			System.out.println("Last Name: " + contact.getLastName());
		}
	}
}
