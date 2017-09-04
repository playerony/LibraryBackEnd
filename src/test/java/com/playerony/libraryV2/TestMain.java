package com.playerony.libraryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.playerony.libraryV2.controller.MainController;
import com.playerony.libraryV2.dao.AuthorAndBookDao;
import com.playerony.libraryV2.dao.AuthorDao;
import com.playerony.libraryV2.dao.BookDao;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.impl.AuthorAndBookDaoImpl;
import com.playerony.libraryV2.impl.AuthorDaoImpl;
import com.playerony.libraryV2.impl.BookDaoImpl;
import com.playerony.libraryV2.model.Author;
import com.playerony.libraryV2.model.Book;
import com.playerony.libraryV2.utility.Connector;

public class TestMain {
	// @Test
	// public void testInsert() {
	// try {
	// Connection connection = Connector.connect(true);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }

	// @Test
	// public void testInsertBatch() {
	// Author author = new Author("test", "test", "M", 50, "12345678912");
	// List<Author> authors = new ArrayList<>();
	// authors.add(author);
	// authors.add(author);
	//
	// AuthorDao authorDao = new AuthorDaoImpl();
	// try {
	// authorDao.insertAuthors(authors);
	// } catch (DatabaseException | InputException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// @Test
	// public void testSelect() {
	// BookDao bookDao = new BookDaoImpl();
	//
	// try {
	// List<Book> books = bookDao.selectBookAndAuthors();
	//
	// for(Book b : books) {
	// System.out.println(b.toString());
	//
	// for(Author a : b.getAuthors())
	// System.out.println(a.toString());
	//
	// System.out.println();
	// }
	// } catch (DatabaseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	
	@Test
	public void testInsertBookAndAuthors() {
		MainController mainController = new MainController();
		
		try {
			mainController.addAuthorsWithBook();
		} catch (InputException | DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSelect() {
		BookDao bookDao = new BookDaoImpl();

		try {
			Book book = bookDao.getBookAndAuthorsById(12L);

			System.out.println(book.toString());

			for (Author a : book.getAuthors())
				System.out.println(a.toString());

			System.out.println();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
