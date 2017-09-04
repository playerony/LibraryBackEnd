package com.playerony.libraryV2.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.playerony.libraryV2.dao.BookDao;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Author;
import com.playerony.libraryV2.model.Book;
import com.playerony.libraryV2.utility.Connector;
import com.playerony.libraryV2.utility.JdbcUtility;
import com.playerony.libraryV2.validate.BookValidate;

public class BookDaoImpl implements BookDao {

	public Connection getConnection() throws SQLException {
		return Connector.connect(true);
	}

	@Override
	public Book insertBook(Book newBook, Connection connection) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		
		final String query = "INSERT INTO BOOKS(ID, TITLE) VALUES(?, ?)";

		try {
			if(connection == null)
				connection = this.getConnection();

			BookValidate.checkBook(newBook);

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			Long id = JdbcUtility.getNextIndexFromSequence(connection, "BOOKS_SEQ");
			if(id == null)
				throw new DatabaseException("Problem by getting a sequence");
			
			newBook.setId(id);
			
			preparedStatement.setLong(i++, id);
			preparedStatement.setString(i++, newBook.getTitle());
			
			int information = preparedStatement.executeUpdate();
			
			if(information != 0)
				System.out.println("Added successfuly: " + newBook.toString());
			else
				System.out.println("I didnt add a new record");
				
			return newBook;
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in insert", e);
		} finally {
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}
	}

	@Override
	public List<Book> insertBooks(List<Book> books, Connection connection) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		List<Book> result = new ArrayList<>();
		
		final String query = "INSERT INTO BOOKS(ID, TITLE) VALUES(?, ?)";

		try {
			if(connection == null)
				connection = this.getConnection();
			
			preparedStatement = connection.prepareStatement(query);

			for (Book b : books) {
				Long id = JdbcUtility.getNextIndexFromSequence(connection, "BOOKS_SEQ");
				if(id == null)
					throw new DatabaseException("Problem by getting a sequence");
				
				b.setId(id);
				
				int i = 1;
				preparedStatement.setLong(i++, id);
				preparedStatement.setString(i++, b.getTitle());
				preparedStatement.addBatch();
				
				result.add(b);
			}

			int[] information = preparedStatement.executeBatch();
			
			for(int i=0 ; i<information.length ;  i++)
				if(information[i] != 0)
					System.out.println("Added: " + books.get(i));
				else
					System.out.println("I didnt add a new batch");
			
			return result;
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in insertBatch", e);
		} finally {
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}
	}

	@Override
	public void updateBook(Long id, Book book) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			if(!checkID(connection, id))
				throw new InputException("I didnt find this id in database");
			
			connection = this.getConnection();
			final String query = "UPDATE BOOKS SET TITLE = ? WHERE ID = ?";

			BookValidate.checkBook(book);

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			preparedStatement.setString(i++, book.getTitle());
			preparedStatement.setLong(i++, id);
			int information = preparedStatement.executeUpdate();
			
			if(information != 0)
				System.out.println("Successful update");
			else
				System.out.println("I cant update this record");
			
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in update", e);
		} finally {
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}
	}

	@Override
	public void deleteBook(Long id) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			if(!checkID(connection, id))
				throw new InputException("I didnt find this id in database");
			
			connection = this.getConnection();
			final String query = "DELETE FROM BOOKS WHERE ID = ?";

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, id);
			int information = preparedStatement.executeUpdate();
			
			if(information != 0)
				System.out.println("Successful deleted");
			else
				System.out.println("I cant delete this record");
			
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in delete", e);
		} finally {
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}
	}

	@Override
	public List<Book> selectBooks() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Book> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM BOOKS";

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			while (resultSet.next()) {
				Long id = resultSet.getLong("ID");
				String title = resultSet.getString("TITLE");

				result.add(new Book(id, title));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in selectAll", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return result;
	}
	
	@Override
	public List<Book> selectBookAndAuthors() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Book> result = new ArrayList<>();
		boolean isToAdd = true;
		Long id = 0L;

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM BOOKS b JOIN AUTHORS a ON a.BOOK_ID = b.ID ORDER BY b.ID";

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			Book book = null;
			
			while (resultSet.next()) {
				if(Long.parseLong(resultSet.getString(1)) != id && book != null) {
					result.add(book);
					isToAdd = true;
				}
				
				id = Long.parseLong(resultSet.getString(1));
				String title = resultSet.getString(2);
				
				if(isToAdd) {
					book = new Book(id, title);
					isToAdd = false;
				}
				
				book.getAuthors().add(new Author(resultSet.getLong(3), 
												 resultSet.getString(4), 
												 resultSet.getString(5), 
												 resultSet.getString(6), 
												 resultSet.getInt(7), 
												 resultSet.getString(8)));
			}
			
			if(book != null)
				result.add(book);
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in selectAll", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return result;
	}
	
	@Override
	public ResultSet getBooksResultSet() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM BOOKS";

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");
			
			return resultSet;
			
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in selectAll", e);
		} finally {
			
		}
	}
	
	@Override
	public List<Long> getUsersByBookID(Long bookID) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Long> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "SELECT u.ID FROM PWOJTASINSKI.AUTHORS u JOIN PWOJTASINSKI.BOOKS b ON b.ID = u.BOOK_ID WHERE u.BOOK_ID = ?";

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, bookID);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Long id = resultSet.getLong("ID");

				result.add(id);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in selectAll", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return result;
	}

	@Override
	public Book getBookById(Long id) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM BOOKS WHERE ID = ?";

			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			if (resultSet.next()) {
				String title = resultSet.getString("TITLE");
				
				if(resultSet.next())
					throw new DatabaseException("I found another book with this id!");

				return new Book(id, title);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in getByID", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return null;
	}
	
	@Override
	public Book getBookAndAuthorsById(Long id) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM BOOKS b JOIN AUTHORS a ON a.BOOK_ID = b.ID WHERE b.ID = ?";

			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");
			
			Book book = null;
			boolean isFirst = true;

			while (resultSet.next()) {
				if(isFirst) {
					book = new Book(id, resultSet.getString(2));
					isFirst = false;
				}
				
				book.getAuthors().add(new Author(resultSet.getLong(3), 
												 resultSet.getString(4), 
												 resultSet.getString(5), 
												 resultSet.getString(6), 
												 resultSet.getInt(7), 
												 resultSet.getString(8)));
			}
			
			return book;
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in getByID", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}
	}
	
	private boolean checkID(Connection connection, Long id) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			final String query = "SELECT * FROM BOOKS WHERE ID = ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()) {
				if(resultSet.next())
					throw new DatabaseException("I found another book with this id!");
				
				return true;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in checkPesel", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return false;
	}

}
