package com.playerony.libraryV2.controller;

import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.impl.BookDaoImpl;
import com.playerony.libraryV2.model.Book;
import com.playerony.libraryV2.validate.Validate;

public class BookController {
	private BookDaoImpl bookDaoImpl;
	
	public BookController() {
		bookDaoImpl = new BookDaoImpl();
	}
	
	public void addBook() throws InputException, DatabaseException {
		System.out.println("\n---- Menu dodawania ksiazki ----");
		String title = Validate.getString("Podaj tytul ksiazki: ");
		
		if(bookDaoImpl.insertBook(new Book(title), null) == null)
			throw new DatabaseException("I didnt add a new Book");
	}
	
	public void updateBook() throws InputException, DatabaseException {
		System.out.println("\n---- Menu aktualizowania ksiazki ----");
		long id = Validate.getInt("Podaj identyfikator ksiazki: ");
		String title = Validate.getString("Podaj tytul ksiazki: ");
		
		bookDaoImpl.updateBook(id, new Book(id, title));
	}
	
	public void deleteBook() throws DatabaseException, InputException {
		System.out.println("\n---- Menu usuwania ksiazki ----");
		long id = Validate.getInt("Podaj identyfikator ksiazki: ");
		
		bookDaoImpl.deleteBook(id);
	}
	
	public void showBooks() throws DatabaseException {
		System.out.println("\n---- Lista ksiazek ----");
		for(Book b : bookDaoImpl.selectBooks())
			System.out.println(b.toString());
	}
	
	public Book getBookById() throws InputException, DatabaseException {
		System.out.println("\n---- Menu usuwania ksiazki ----");
		Long id = (long) Validate.getInt("Podaj identyfikator ksiazki: ");
		
		return bookDaoImpl.getBookById(id);
	}

	public BookDaoImpl getBookDaoImpl() {
		return bookDaoImpl;
	}
	
	
}
