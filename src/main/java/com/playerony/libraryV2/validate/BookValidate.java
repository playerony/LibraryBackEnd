package com.playerony.libraryV2.validate;

import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Book;

public class BookValidate {
	public static void checkTitle(String title) throws InputException {
		if(!Validate.checkNumbersInString(title))
			throw new InputException("Wrong marks in title");
		
		if(title.length() < 2)
			throw new InputException("Title is too short");
		
		if(title.length() > 30)
			throw new InputException("Title is too long");
	}
	
	public static void checkBook(Book book) throws InputException{
		checkTitle(book.getTitle());
	}
}
