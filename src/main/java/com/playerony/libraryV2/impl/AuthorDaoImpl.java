package com.playerony.libraryV2.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mysql.jdbc.CallableStatement;

import com.mysql.jdbc.Statement;
import com.playerony.libraryV2.dao.AuthorDao;
import com.playerony.libraryV2.dao.Copy;
import com.playerony.libraryV2.dao.SqlInjection;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Author;
import com.playerony.libraryV2.utility.Connector;
import com.playerony.libraryV2.utility.JdbcUtility;
import com.playerony.libraryV2.validate.AuthorValidation;

public class AuthorDaoImpl implements AuthorDao, SqlInjection, Copy {

	public Connection getConnection() throws SQLException {
		return Connector.connect(true);
	}

	@Override
	public Author insertAuthor(Author newAuthor, Connection connection) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		
		final String query = "INSERT INTO AUTHORS(ID, FIRST_NAME, LAST_NAME, GENDER, AGE, PESEL, BOOK_ID) VALUES(?, ?, ?, ?, ?, ?, ?)";

		try {
			if(!checkPesel(connection, newAuthor.getPesel()))
				throw new InputException("I found this pesel in database");
			
			if(connection == null)
				connection = this.getConnection();
			
			AuthorValidation.checkAuthor(newAuthor);

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			Long id = JdbcUtility.getNextIndexFromSequence(connection, "AUTHORS_SEQ");
			if(id == null)
				throw new DatabaseException("Problem by getting a sequence");
			
			newAuthor.setId(id);
			
			preparedStatement.setLong(i++, id);
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
	public List<Author> insertAuthors(List<Author> authors, Connection connection) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		List<Author> result = new ArrayList<>();

		try {
			if(connection == null)
				connection = this.getConnection();
			
			if(checkInsertBatch(connection, authors))
				throw new InputException("I found this pesel in database");
			
			final String query = "INSERT INTO AUTHORS(ID, FIRST_NAME, LAST_NAME, GENDER, AGE, PESEL, BOOK_ID) VALUES(?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(query);
			
			for (Author a : authors) {
				Long id = JdbcUtility.getNextIndexFromSequence(connection, "AUTHORS_SEQ");
				if(id == null)
					throw new DatabaseException("Problem by getting a next sequence");
				
				a.setId(id);
				
				int i = 1;
				preparedStatement.setLong(i++, id);
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
	public void updateAuthor(Long id, Author author) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			if(!checkID(connection, id))
				throw new InputException("I didnt find this id in database");
			
			connection = this.getConnection();
			final String query = "UPDATE AUTHORS SET FIRST_NAME = ?, LAST_NAME = ?, GENDER = ?, AGE = ?, BOOK_ID = ? WHERE ID = ?";

			AuthorValidation.checkAuthor(author);

			preparedStatement = connection.prepareStatement(query);
			int i = 1;
			preparedStatement.setString(i++, author.getFirstName());
			preparedStatement.setString(i++, author.getLastName());
			preparedStatement.setString(i++, author.getGender());
			preparedStatement.setInt(i++, author.getAge());
			preparedStatement.setInt(i++, author.getBookID());
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
	public void deleteAuthor(Long id) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			if(!checkID(connection, id))
				throw new InputException("I didnt find this id in database");
			
			connection = this.getConnection();
			final String query = "DELETE FROM AUTHORS WHERE ID = ?";

			preparedStatement = connection.prepareStatement(query);
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
	public List<Author> selectAuthors() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Author> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM AUTHORS";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			while (resultSet.next()) {
				Long id = resultSet.getLong("ID");
				String firstName = resultSet.getString("FIRST_NAME");
				String lastName = resultSet.getString("LAST_NAME");
				String gender = resultSet.getString("GENDER");
				Integer age = resultSet.getInt("AGE");
				String pesel = resultSet.getString("PESEL");
				Integer bookID = resultSet.getInt("BOOK_ID");

				result.add(new Author(id, firstName, lastName, gender, age, pesel, bookID));
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
	public ResultSet getAuthorsResultSet() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM AUTHORS";

			preparedStatement = connection.prepareStatement(query);
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
	public Author getAuthorById(Long id) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			if(!checkID(connection, id))
				throw new InputException("I didnt find this author in database");
			
			connection = this.getConnection();
			final String query = "SELECT * FROM AUTHORS WHERE ID = ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			if (resultSet.next()) {
				String firstName = resultSet.getString("FIRST_NAME");
				String lastName = resultSet.getString("LAST_NAME");
				String gender = resultSet.getString("GENDER");
				Integer age = resultSet.getInt("AGE");
				String pesel = resultSet.getString("PESEL");
				Integer bookID = resultSet.getInt("BOOK_ID");
				
				if(resultSet.next())
					throw new DatabaseException("I found another author with this id!");

				return new Author(id, firstName, lastName, gender, age, pesel, bookID);
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
	public List<Author> getAuthorByAge(Integer age) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Author> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM AUTHORS WHERE AGE = ?";

			preparedStatement = (PreparedStatement) connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, age);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			while (resultSet.next()) {
				Long id = resultSet.getLong("ID");
				String firstName = resultSet.getString("FIRST_NAME");
				String lastName = resultSet.getString("LAST_NAME");
				String gender = resultSet.getString("GENDER");
				String pesel = resultSet.getString("PESEL");
				Integer bookID = resultSet.getInt("BOOK_ID");

				result.add(new Author(id, firstName, lastName, gender, age, pesel, bookID));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in getByAge", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return result;
	}

	@Override
	public List<Author> getAuthorByFirstName(String firstName) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Author> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM AUTHORS WHERE UPPER(FIRST_NAME) LIKE UPPER(?)";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, firstName);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			while (resultSet.next()) {
				Long id = resultSet.getLong("ID");
				String lastName = resultSet.getString("LAST_NAME");
				String gender = resultSet.getString("GENDER");
				Integer age = resultSet.getInt("AGE");
				String pesel = resultSet.getString("PESEL");
				Integer bookID = resultSet.getInt("BOOK_ID");

				result.add(new Author(id, firstName, lastName, gender, age, pesel, bookID));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in getByFirstName", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return result;
	}

	@Override
	public List<Author> getAuthorByLastName(String lastName) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Author> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM AUTHORS WHERE UPPER(LAST_NAME) LIKE UPPER(?)";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, lastName);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			while (resultSet.next()) {
				Long id = resultSet.getLong("ID");
				String firstName = resultSet.getString("FIRST_NAME");
				String gender = resultSet.getString("GENDER");
				Integer age = resultSet.getInt("AGE");
				String pesel = resultSet.getString("PESEL");
				Integer bookID = resultSet.getInt("BOOK_ID");

				result.add(new Author(id, firstName, lastName, gender, age, pesel, bookID));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in getByLastName", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return result;
	}

	@Override
	public List<Author> selectInjection(String phrase) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Author> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM AUTHORS WHERE FIRST_NAME = " + phrase;

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			while (resultSet.next()) {
				Long id = resultSet.getLong("ID");
				String firstName = resultSet.getString("FIRST_NAME");
				String lastName = resultSet.getString("LAST_NAME");
				String gender = resultSet.getString("GENDER");
				Integer age = resultSet.getInt("AGE");
				String pesel = resultSet.getString("PESEL");
				Integer bookID = resultSet.getInt("BOOK_ID");

				result.add(new Author(id, firstName, lastName, gender, age, pesel, bookID));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in selectInjection", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return result;
	}

	@Override
	public void copyAuthorTable() throws DatabaseException {
		Connection connection = null;

		try {
			connection = this.getConnection();
			final String query = "{call copy_table ()}";
			CallableStatement stmt = (CallableStatement) connection.prepareCall(query);
			//stmt.setInt(1, 5);
			stmt.execute();

		} catch (Exception e) {
			throw new DatabaseException("Database exception in copyTable", e);
		} finally {
			Connector.closeConnection(connection);
		}
	}
	
	private boolean checkID(Connection connection, Long id) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM AUTHORS WHERE ID = ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next())
				return true;
			
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in checkID", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
		}

		return false;
	}
	
	public static boolean checkInsertBatch(Connection connection, List<Author> authors) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			String result = "";
			Iterator itr = authors.iterator();
			
			while(itr.hasNext()) {
				itr.next();
				result += "?";
				
				if(itr.hasNext())
					result += ", ";
			}
			
			final String query = "SELECT * FROM AUTHORS WHERE PESEL IN (" + result + ")";
			System.out.println(query);
			
			itr = authors.iterator();
			System.out.println("work");
			preparedStatement = connection.prepareStatement(query);
			
			System.out.println("work");
			
			for(int i=0 ; i<authors.size() ; i++) {
				System.out.println(authors.get(i).getPesel());
				
				preparedStatement.setString(i + 1, authors.get(i).getPesel());
			}
			
			System.out.println("work");
			resultSet = preparedStatement.executeQuery();
			System.out.println("work");

			if(resultSet.next())
				return true;
			
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in checkPesel", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
		}
		
		return false;
	}
	
	private boolean checkPesel(Connection connection, String pesel) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			final String query = "SELECT * FROM AUTHORS WHERE PESEL LIKE ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, pesel);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next())
				return true;
			
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in checkPesel", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
		}

		return false;
	}

}
