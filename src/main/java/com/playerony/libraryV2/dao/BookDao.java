package com.playerony.libraryV2.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Book;

public interface BookDao {
	Book insertBook(Book newBook, Connection connection) throws DatabaseException, InputException;
	
	List<Book> insertBooks(List<Book> entities, Connection connection) throws DatabaseException;
	
	void updateBook(Long id, Book book) throws DatabaseException, InputException ;
	
	void deleteBook(Long id) throws DatabaseException, InputException;
	
	List<Book> selectBooks() throws DatabaseException;
	
	List<Book> selectBookAndAuthors() throws DatabaseException;
	
	Book getBookById(Long id) throws DatabaseException;
	
	Book getBookAndAuthorsById(Long id) throws DatabaseException;
	
	List<Long> getUsersByBookID(Long id) throws DatabaseException;
	
	ResultSet getBooksResultSet() throws DatabaseException;
}
