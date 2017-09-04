package com.playerony.libraryV2.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.playerony.libraryV2.dao.CopyAuthorDao;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Author;
import com.playerony.libraryV2.utility.Connector;
import com.playerony.libraryV2.utility.JdbcUtility;
import com.playerony.libraryV2.validate.AuthorValidation;

public class CopyAuthorDaoImpl implements CopyAuthorDao {

	public Connection getConnection() throws SQLException {
		return Connector.connect(true);
	}
	
	@Override
	public Author insertCopyAuthor(Author newAuthor) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			if(checkPesel(newAuthor.getPesel()) != -1)
				throw new InputException("I found this pesel in database");
			
			connection = this.getConnection();
			final String query = "INSERT INTO COPY_AUTHORS(ID, FIRST_NAME, LAST_NAME, GENDER, AGE, PESEL, BOOK_ID) VALUES(?, ?, ?, ?, ?, ?, ?)";

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			Long id = getNextIndexFromSequence();
			if(id == null)
				throw new DatabaseException("Problem by getting a sequence");
			
			newAuthor.setId(id);
			
			preparedStatement.setLong(i++, newAuthor.getId());
			preparedStatement.setString(i++, newAuthor.getFirstName());
			preparedStatement.setString(i++, newAuthor.getLastName());
			preparedStatement.setString(i++, newAuthor.getGender());
			preparedStatement.setInt(i++, newAuthor.getAge());
			preparedStatement.setString(i++, newAuthor.getPesel());
			preparedStatement.setInt(i++, newAuthor.getBookID());
			int information = preparedStatement.executeUpdate();
			
			if(information != 0)
				System.out.println("Added successfuly: " + newAuthor.toString());
			else
				System.out.println("I didnt add a new record");
				
			return newAuthor;
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in insert", e);
		} finally {
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}
	}

	@Override
	public List<Author> insertCopyAuthors(List<Author> authors) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		List<Author> result = new ArrayList<>();

		try {
			for(Author a : authors) {
				if(checkPesel(a.getPesel()) != -1)
					throw new InputException("I found this pesel in database");
				
				if(a.getBookID() == null)
					throw new InputException("BookID is a null");
			}
			
			connection = this.getConnection();
			final String query = "INSERT INTO COPY_AUTHORS(ID, FIRST_NAME, LAST_NAME, GENDER, AGE, PESEL, BOOK_ID) VALUES(?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(query);
			
			for (Author a : authors) {
				int i = 1;
				Long id = getNextIndexFromSequence();
				if(id == null)
					throw new DatabaseException("Problem by getting a sequence");
				
				a.setId(id);
				
				preparedStatement.setLong(i++, a.getId());
				preparedStatement.setString(i++, a.getFirstName());
				preparedStatement.setString(i++, a.getLastName());
				preparedStatement.setString(i++, a.getGender());
				preparedStatement.setInt(i++, a.getAge());
				preparedStatement.setString(i++, a.getPesel());
				preparedStatement.setInt(i++, a.getBookID());
				preparedStatement.addBatch();
				
				result.add(a);
			}

			int[] information = preparedStatement.executeBatch();
			
			for(int i=0 ; i<information.length ;  i++) 
				if(information[i] != 0)
					System.out.println("Added: " + authors.get(i));
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
	public void deleteCopyAuthors() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			connection = this.getConnection();
			final String query = "DELETE FROM COPY_AUTHORS";

			preparedStatement = connection.prepareStatement(query);
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
	
	private int checkPesel(String pesel) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM COPY_AUTHORS WHERE PESEL LIKE ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, pesel);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next())
				return resultSet.getInt("ID");
			
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in checkPesel", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return -1;
	}
	
	private Long getNextIndexFromSequence() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT COPY_AUTHORS_SEQ.NEXTVAL FROM DUAL";

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
