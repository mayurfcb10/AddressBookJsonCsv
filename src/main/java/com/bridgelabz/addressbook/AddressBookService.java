package com.bridgelabz.addressbook;

import java.time.LocalDate;
import java.util.List;

public class AddressBookService {
	private List<ContactDetails> contactList;

	enum IOService {
		DB_IO
	}

	public List<ContactDetails> readAddressBookData(IOService ioService) throws AddressBookException {
		if (ioService.equals(IOService.DB_IO)) {
			this.contactList = new AddressBookDBService().readData();
		}
		return this.contactList;
	}

	/*method to update contact information in Database*/
    public void updateContactInformation(String firstName, String lastName) throws AddressBookException {
        int result = new AddressBookDBService().updateContactData(firstName, lastName);
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
    public boolean checkEmployeePayrollInSyncWithDB(String firstName) throws AddressBookException {
        List<ContactDetails> contactList = new AddressBookDBService().getContactData(firstName);
        return contactList.get(0).equals(getContactData(firstName));
    }
    
    public List<ContactDetails> readContactDataForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) throws AddressBookException {
        if(ioService.equals(IOService.DB_IO))
           this.contactList = new AddressBookDBService().readContactDataForDateRange(startDate,endDate);
        return this.contactList;
    }


}
