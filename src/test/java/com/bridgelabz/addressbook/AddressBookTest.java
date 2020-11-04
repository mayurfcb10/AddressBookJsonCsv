package com.bridgelabz.addressbook;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bridgelabz.addressbook.AddressBookService.IOService;

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
	
	 /*Test Case to update the contact details updated in database using JDBC*/
    @Test
    public void givenNewInformationOfContact_WhenUpdatedShouldMatch() throws AddressBookException {
        List<ContactDetails> contactList = addressBookService.readAddressBookData(DB_IO);
        addressBookService.updateContactInformation("Terisa","Morgan");
        boolean result = addressBookService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }
    
    /* TestCase to check the contacts added in the given date range*/
    @Test
    public void givenDateRangeWhenRetrievedShouldMatchContactCount() throws AddressBookException {
        addressBookService.readAddressBookData(DB_IO);
        LocalDate startDate = LocalDate.of(2019,8,23);
        LocalDate endDate = LocalDate.now();
        List<ContactDetails> contactList = addressBookService.readContactDataForDateRange(DB_IO, startDate, endDate);
        Assert.assertEquals(2,contactList.size());
    }
    
    @Test 
    public void givenState_shouldReturnThePersonBelongingToThatState() throws AddressBookException {
    	AddressBookService addressBookService = new AddressBookService();
    	Map<String, String> getPersonDetailMap = addressBookService.readContactByStateOrCity(DB_IO,"state");
    	Assert.assertTrue(getPersonDetailMap.get("California").equals("Charlie")&& getPersonDetailMap.get("MP").equals("Terisa"));	
    }
    
    @Test 
    public void givenState_shouldReturnThePersonBelongingToThatCity() throws AddressBookException {
    	AddressBookService addressBookService = new AddressBookService();
    	Map<String, String> getPersonDetailMap = addressBookService.readContactByStateOrCity(DB_IO,"city");
    	Assert.assertTrue(getPersonDetailMap.get("Indore").equals("Bill"));	
    }
}
