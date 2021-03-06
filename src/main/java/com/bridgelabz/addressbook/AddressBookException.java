package com.bridgelabz.addressbook;

public class AddressBookException extends Exception {
	
	    enum ExceptionType{
	        SQL_EXCEPTION, RETRIEVAL_PROBLEM, UPDATE_PROBLEM, CONNECTION_PROBLEM, INSERTION_PROBLEM, ROLLBACK_PROBLEM
	    }
	    ExceptionType type;

	    public AddressBookException(String message, ExceptionType type) {
	        super(message);
	        this.type = type;
	    }
}
