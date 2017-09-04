package com.playerony.libraryV2.validate;

import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Author;

public class AuthorValidation {
	public static void checkFirstName(String firstName) throws InputException {
		if(!Validate.checkNumbersInString(firstName))
			throw new InputException("Wrong marks in firstName");
		
		if(firstName.length() < 2)
			throw new InputException("FirstName is too short");
		
		if(firstName.length() > 30)
			throw new InputException("FirstName is too long");
	}
	
	public static void checkLastName(String lastName) throws InputException {
		if(!Validate.checkNumbersInString(lastName))
			throw new InputException("Wrong marks in lastName");
		
		if(lastName.length() < 2)
			throw new InputException("LastName is too short");
		
		if(lastName.length() > 30)
			throw new InputException("LastName is too long");
	}
	
	public static void checkAge(int age) throws InputException{
		if(age < 0 || age > 100)
			throw new InputException("Wrong age(between 1 and 100)");
	}
	
	public static void checkGender(String gender) throws InputException{
		if(!gender.equals("M") && !gender.equals("K"))
			throw new InputException("Wrong gender");
	}
	
	public static void checkAuthor(Author author) throws InputException{
		checkFirstName(author.getFirstName());
		checkLastName(author.getLastName());
		checkGender(author.getGender());
		checkAge(author.getAge());
		Validate.checkPesel(author.getPesel());
	}
}
