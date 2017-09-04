package com.playerony.libraryV2.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Author;

public interface AuthorDao {
	Author insertAuthor(Author newAuthor, Connection connection) throws DatabaseException, InputException;
	
	List<Author> insertAuthors(List<Author> authors, Connection connection) throws DatabaseException, InputException;
	
	void updateAuthor(Long id, Author author) throws DatabaseException, InputException;
	
	void deleteAuthor(Long id) throws DatabaseException, InputException;
	
	List<Author> selectAuthors() throws DatabaseException;
	
	Author getAuthorById(Long id) throws DatabaseException, InputException;
	
	List<Author> getAuthorByAge(Integer age) throws DatabaseException;
	
	List<Author> getAuthorByFirstName(String firstName) throws DatabaseException;
	
	List<Author> getAuthorByLastName(String lastName) throws DatabaseException;
	
	List<Long> getUsersByBookID(Long id) throws DatabaseException;
	
	ResultSet getAuthorsResultSet() throws DatabaseException;
}
