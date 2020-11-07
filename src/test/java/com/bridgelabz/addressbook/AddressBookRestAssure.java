package com.bridgelabz.addressbook;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bridgelabz.addressbook.AddressBookService.IOService;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AddressBookRestAssure {
static AddressBookService addressBookService;
	
	@BeforeClass
	public static void initializeConstructor() {
		 addressBookService = new AddressBookService();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Before
	public  void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}
	
	/* get Contact List from the AddressBook Json */
	public ContactDetails[] getContactList(){
        Response response = RestAssured.get("/contacts");
        System.out.println("Employee Payroll entires in JSON Server: \n"+response.asString());
        ContactDetails[] arrayOfContacts = new Gson().fromJson(response.asString(),ContactDetails[].class);
        return arrayOfContacts;
    }
	
	 @Test
	    public void givenEmployeeDataInJSONServer_whenRetrieved_shouldMatchTheCount(){
	        ContactDetails[] arrayOfContacts = getContactList();
	        AddressBookService addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
	        long entries = addressBookService.countEntries(IOService.REST_IO);
	        Assert.assertEquals(2,entries);
	    }

}
