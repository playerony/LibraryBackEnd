package com.playerony.libraryV2.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.playerony.libraryV2.dao.CopyBookDao;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Book;
import com.playerony.libraryV2.utility.Connector;
import com.playerony.libraryV2.utility.JdbcUtility;
import com.playerony.libraryV2.validate.BookValidate;

public class CopyBookDaoImpl implements CopyBookDao {

	public Connection getConnection() throws SQLException {
		return Connector.connect(true);
	}
	
	@Override
	public Book insertCopyBook(Book newBook) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			connection = this.getConnection();
			final String query = "INSERT INTO COPY_BOOKS(ID, TITLE) VALUES(?, ?)";

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			Long id = getNextIndexFromSequence();
			if(id == null)
				throw new DatabaseException("Problem by getting a sequence");
			
			newBook.setId(id);
			
			preparedStatement.setLong(i++, newBook.getId());
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
	public List<Book> insertCopyBooks(List<Book> books) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		List<Book> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "INSERT INTO COPY_BOOKS(ID, TITLE) VALUES(?, ?)";
			preparedStatement = connection.prepareStatement(query);

			for (Book b : books) {
				int i = 1;
				Long id = getNextIndexFromSequence();
				if(id == null)
					throw new DatabaseException("Problem by getting a sequence");
				
				b.setId(id);
				
				preparedStatement.setLong(i++, b.getId());
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
	public void deleteCopyBooks() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			connection = this.getConnection();
			final String query = "DELETE FROM COPY_BOOKS";

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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
	
	private Long getNextIndexFromSequence() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT COPY_BOOKS_SEQ.NEXTVAL FROM DUAL";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			if(resultSet.next())
				return resultSet.getLong(1);
			
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in checkPesel", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}
		
		return null;
	}

}
