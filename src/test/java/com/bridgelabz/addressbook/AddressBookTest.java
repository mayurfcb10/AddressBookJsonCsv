package com.bridgelabz.addressbook;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bridgelabz.addressbook.AddressBookService.IOService;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static com.bridgelabz.addressbook.AddressBookService.IOService.DB_IO;

public class AddressBookTest {
	AddressBookService addressBookService;

	@Before

	public void setUp() {
		addressBookService = new AddressBookService();
	}

	/* Test Case to check the number of retrieved contacts from database */
	@Test
	public void givenAdddressBook_WhenRecordsRetrieved_ShouldReturnCount() throws AddressBookException {
		List<ContactDetails> contactList = addressBookService.readAddressBookData(DB_IO);
		Assert.assertEquals(3, contactList.size());
	}

	/* Test Case to update the contact details updated in database using JDBC */
	@Test
	public void givenNewInformationOfContact_WhenUpdatedShouldMatch() throws AddressBookException {
		List<ContactDetails> contactList = addressBookService.readAddressBookData(DB_IO);
		addressBookService.updateContactInformation("Terisa", "Morgan");
		boolean result = addressBookService.checkAddressBookInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}

	/* TestCase to check the contacts added in the given date range */
	@Test
	public void givenDateRangeWhenRetrievedShouldMatchContactCount() throws AddressBookException {
		addressBookService.readAddressBookData(DB_IO);
		LocalDate startDate = LocalDate.of(2019, 8, 23);
		LocalDate endDate = LocalDate.now();
		List<ContactDetails> contactList = addressBookService.readContactDataForDateRange(DB_IO, startDate, endDate);
		Assert.assertEquals(2, contactList.size());
	}

	/* TestCase to check the person belonging to the particular state */
	@Test
	public void givenState_shouldReturnThePersonBelongingToThatState() throws AddressBookException {
		Map<String, String> getPersonDetailMap = addressBookService.readContactByStateOrCity(DB_IO, "state");
		Assert.assertTrue(getPersonDetailMap.get("California").equals("Charlie")
				&& getPersonDetailMap.get("MP").equals("Terisa"));
	}

	@Test
	public void givenState_shouldReturnThePersonBelongingToThatCity() throws AddressBookException {
		Map<String, String> getPersonDetailMap = addressBookService.readContactByStateOrCity(DB_IO, "city");
		Assert.assertTrue(getPersonDetailMap.get("Indore").equals("Bill"));
	}

	@Test
	public void givenContactToBeAdded_shouldreturntheContactAddedinDBandinSyncWithDB() throws AddressBookException {
		addressBookService.addContactDetails("Rahul", "Dixit", "Housing", "Mhow", "Maharastra", "rahul.eee18@bhu.in",
				"6868686887", "400007", 4, 103, "professional.", LocalDate.of(2019, 8, 28));
		boolean result = addressBookService.checkAddressBookInSyncWithDB("Rahul");
		Assert.assertTrue(result);
	}

	public void givenContactsToBeAdded_shouldReturnTheContactAddedInDB_shouldMatchDBEntries()
			throws AddressBookException {
		ContactDetails[] contactDetailsArray = {
				new ContactDetails("Sharan", "Alawa", "Indus Town", "Rajannagar", "Maharastra",
						"sharan.eee18@gmail.com", "9354895489", "400037", 5, 105, "friend", LocalDate.of(2018, 8, 28)),
				new ContactDetails("Gerrard", "Pique", "Barca", "Barcelona", "BarcaState", "gerrard@fcb.in",
						"87888888775", "404507", 6, 106, "professional.", LocalDate.of(2016, 3, 25)),
				new ContactDetails("Rahul", "Dixit", "Housing", "Mhow", "Maharastra", "rahul.eee18@bhu.in",
						"6868686887", "400007", 4, 104, "professional.", LocalDate.of(2019, 8, 28))

		};
		addressBookService.readAddressBookData(IOService.DB_IO);
		Instant start = Instant.now();
		addressBookService.addContactsToAddressBook(Arrays.asList(contactDetailsArray));
		Instant end = Instant.now();
		System.out.println("Duration without Thread; " + Duration.between(start, end));
		Instant threadStart = Instant.now();
		addressBookService.addContactsToAddressBookWithThreads(Arrays.asList(contactDetailsArray));
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread; " + Duration.between(threadStart, threadEnd));
		Assert.assertEquals(6, addressBookService.countEntries(IOService.DB_IO));
	}
}
