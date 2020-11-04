package com.bridgelabz.addressbook;

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

}
