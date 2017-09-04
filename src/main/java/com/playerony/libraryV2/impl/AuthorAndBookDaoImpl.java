package com.playerony.libraryV2.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.playerony.libraryV2.dao.AuthorAndBookDao;
import com.playerony.libraryV2.dao.AuthorDao;
import com.playerony.libraryV2.dao.BookDao;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Author;
import com.playerony.libraryV2.model.Book;
import com.playerony.libraryV2.utility.Connector;
import com.playerony.libraryV2.validate.AuthorValidation;
import com.playerony.libraryV2.validate.BookValidate;

public class AuthorAndBookDaoImpl implements AuthorAndBookDao {
	private BookDao bookDao;
	private AuthorDaoImpl authorDaoImpl;
	
	public AuthorAndBookDaoImpl() {
		bookDao = new BookDaoImpl();
		authorDaoImpl = new AuthorDaoImpl();
	}

	public Connection getConnection() throws SQLException {
		return Connector.connect(true);
	}
	
	@Override
	public void insertBookAndAuthors(Book book, List<Author> authors) throws InputException, DatabaseException {
		Connection connection;
		try {
			connection = getConnection();
		} catch (SQLException e) {
			throw new DatabaseException("Problem by connection getting", e);
		}
		
		validateBooksAndAuthors(book, authors, connection);
		
		bookDao.insertBook(book, connection);
		authorDaoImpl.insertAuthors(authors, connection);
	}

	private boolean validateBooksAndAuthors(Book book, List<Author> authors, Connection connection) throws InputException, DatabaseException {
		BookValidate.checkBook(book);
		
		for(Author a : authors)
			AuthorValidation.checkAuthor(a);
		
		authorDaoImpl.checkInsertBatch(connection, authors);
		
		bookDao.insertBook(book, null);
		authorDaoImpl.insertAuthors(authors, null);
		
		return true;
	}
	
}
