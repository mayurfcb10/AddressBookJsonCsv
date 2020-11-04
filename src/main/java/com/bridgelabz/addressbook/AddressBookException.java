package com.bridgelabz.addressbook;

public class AddressBookException extends Exception {
	
	    enum ExceptionType{
	        SQL_EXCEPTION, RETRIEVAL_PROBLEM
	    }
	    ExceptionType type;

	    public AddressBookException(String message, ExceptionType type) {
	        super(message);
	        this.type = type;
	    }
}
