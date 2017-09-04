package com.playerony.libraryV2.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import com.playerony.libraryV2.dao.AuthorAndBookDao;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.impl.AuthorAndBookDaoImpl;
import com.playerony.libraryV2.model.Author;
import com.playerony.libraryV2.model.Book;

public class MainController {
	private BookController bookController;
	private AuthorController authorController;
	private AuthorAndBookDao authorAndBookDao;
	
	public MainController() {
		bookController = new BookController();
		authorController = new AuthorController();
		authorAndBookDao = new AuthorAndBookDaoImpl();
	}
	
	public void addAuthorsWithBook() throws InputException, DatabaseException {
		//TODO
		//pobranie od użytkownika nazwy książki
		//pobranie od użytkownika danych autora
		//wywołanie metody DAO zapisującej to do bazy
		
//		bookController.addBook();
//		
//		Book book = bookController.getBookById();
//		if(book != null)
//			authorController.addAuthor(book.getId().intValue());
//		else
//			throw new InputException("I didnt find this book in db");
		
		Book book = new Book("Lord of the rings");
		
		List<Author> authors = new ArrayList<Author>();
		authors.add(new Author("TEST", "TEST", "M", 56, "22222222222"));
		authors.add(new Author("TEST", "TEST", "M", 65, "33333333333"));
		
		authorAndBookDao.insertBookAndAuthors(book, authors);
	}
	
	public void selectAuthorsAndBooks() throws DatabaseException {
		for(Author a : authorController.getAuthorDaoImpl().selectAuthors()) {
			System.out.println(a.toString());
			
			for(Book b : bookController.getBookDaoImpl().selectBooks()) {
				if(a.getBookID() == b.getId().longValue()) {
					System.out.println(b.toString());
				}
			}
			
			System.out.println("");
		}
	}
}
