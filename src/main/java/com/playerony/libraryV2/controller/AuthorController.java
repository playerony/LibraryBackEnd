package com.playerony.libraryV2.controller;

import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.impl.AuthorDaoImpl;
import com.playerony.libraryV2.model.Author;
import com.playerony.libraryV2.validate.Validate;

public class AuthorController {
	private AuthorDaoImpl authorDaoImpl;
	
	public AuthorController() {
		authorDaoImpl = new AuthorDaoImpl();
	}
	
	public void addAuthor(Integer bookID) throws InputException, DatabaseException {
		System.out.println("\n---- Menu dodawania autora ----");
		String firstName = Validate.getString("Podaj imie autora: ");
		String lastName = Validate.getString("Podaj nazwisko autora: ");
		String gender = Validate.getString("Podaj plec autora: ");
		Integer age = Validate.getInt("Podaj wiek autora: ");
		String pesel = Validate.getString("Podaj pesel autora: ");
		
		if(authorDaoImpl.insertAuthor(new Author(firstName, lastName, gender, age, pesel, bookID), null) == null)
			throw new DatabaseException("I didnt add a new Author");
	}
	
	public void updateAuthor() throws InputException, DatabaseException {
		System.out.println("\n---- Menu aktualizowania stanu autora ----");
		long id = Validate.getInt("Podaj identyfikator autora: ");
		String firstName = Validate.getString("Podaj imie autora: ");
		String lastName = Validate.getString("Podaj nazwisko autora: ");
		String gender = Validate.getString("Podaj plec autora: ");
		int age = Validate.getInt("Podaj wiek autora: ");
		String pesel = Validate.getString("Podaj pesel autora: ");
		
		authorDaoImpl.updateAuthor(id, new Author(firstName, lastName, gender, age, pesel));
	}
	
	public void deleteAuthor() throws InputException, DatabaseException {
		System.out.println("\n---- Menu usuwania autora ----");
		long id = Validate.getInt("Podaj identyfikator autora: ");
		
		authorDaoImpl.deleteAuthor(id);
	}
	
	public void showAuthors() throws DatabaseException {
		System.out.println("\n---- Lista uzytkownikow ----");
		for(Author a : authorDaoImpl.selectAuthors())
			System.out.println(a.toString());
	}
	
	public void showInjection() throws DatabaseException {
		System.out.println("\n---- Menu injection autora ----");
		String phrase = Validate.getString("Podaj wyrazenie: ");
		
		for(Author a : authorDaoImpl.selectInjection(phrase))
			System.out.println(a.toString());
	}
	
	public void copyTable() throws DatabaseException {
		System.out.println("\n---- Wywolywanie procedury z bazy danych ----");
		authorDaoImpl.copyAuthorTable();
	}
	
	public void getByID() throws DatabaseException, InputException {
		System.out.println("\n---- Pobieranie autora po id z bazy danych ----");
		Long id = (long) Validate.getInt("Podaj identyfikator autora: ");
		Author author = authorDaoImpl.getAuthorById(id);
		System.out.println(author.toString());
	}

	public AuthorDaoImpl getAuthorDaoImpl() {
		return authorDaoImpl;
	}
	
	
}
