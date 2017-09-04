package com.playerony.libraryV2.exception;

public class DatabaseException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public DatabaseException(String message){
		super(message);
	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
