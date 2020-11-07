package com.bridgelabz.addressbook;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bridgelabz.addressbook.AddressBookService.IOService;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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
        System.out.println("Address Book entires in JSON Server: \n"+response.asString());
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
	 
	 
	 private Response addContactToJsonServer(ContactDetails contactData) {
			String employeeJson = new Gson().toJson(contactData);
			RequestSpecification request = RestAssured.given();
			request.header("Content-Type", "application/json");
			request.body(employeeJson);
			return request.post("/contacts");
		}
		
		/* Added Contact in Employee Payroll Json */
		@Test
		public void givenContactListWhenAdded_shouldMatch201andCount() throws AddressBookException {
			ContactDetails[] arrayOfEmps = getContactList();
			addressBookService = new AddressBookService(Arrays.asList(arrayOfEmps));
			
			ContactDetails contactData = null;
			contactData = new ContactDetails("Sharan", "Alawa", "Indus Town", "Rajannagar", "Maharastra",
					"sharan.eee18@gmail.com", "9354895489", "400037", 3, 105, "friend", LocalDate.of(2018, 8, 28));
			
			Response response = addContactToJsonServer(contactData);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);
			
			contactData = new Gson().fromJson(response.asString(), ContactDetails.class);
			addressBookService.addContactDetails(contactData, IOService.REST_IO);
		 long entries = addressBookService.countEntries(AddressBookService.IOService.REST_IO);
		 Assert.assertEquals(3,entries);
			
		}
		
		/* Updated data of contact in Employee Payroll Json */
		@Test
		public void givenFirstNameandLastName_whenUpdated_ShouldMatch200Response() throws AddressBookException {
			ContactDetails[] arrayOfEmps = getContactList();
			addressBookService = new AddressBookService(Arrays.asList(arrayOfEmps));
			
			addressBookService.updateContactInformation("Sharan","Ahluwalia",IOService.REST_IO);
			ContactDetails contactData = addressBookService.getContactData("Sharan");
			
			String employeeJson = new Gson().toJson(contactData);
			RequestSpecification request = RestAssured.given();
			request.header("Content-Type", "application/json");
			request.body(employeeJson);
			Response response = request.put("/contacts/"+contactData.userId);
			int statusCode = response.getStatusCode();
	    	Assert.assertEquals(200, statusCode);
		}
}
