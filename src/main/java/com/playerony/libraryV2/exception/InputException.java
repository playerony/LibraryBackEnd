package com.playerony.libraryV2.exception;

public class InputException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InputException(){
		super("Input Exception");
	}
	
	public InputException(String message){
		super(message);
	}
}
