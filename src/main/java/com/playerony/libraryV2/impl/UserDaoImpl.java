package com.playerony.libraryV2.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.playerony.libraryV2.dao.UserDao;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.User;
import com.playerony.libraryV2.utility.Connector;
import com.playerony.libraryV2.utility.JdbcUtility;

public class UserDaoImpl implements UserDao {

	public Connection getConnection() throws SQLException {
		return Connector.connect(true);
	}
	
	@Override
	public List<User> selectUsers() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<User> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM USERS";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			while (resultSet.next()) {
				Long id = resultSet.getLong("ID");
				String username = resultSet.getString("USER_NAME");
				String password = resultSet.getString("PASSWORD");
				Integer enable = resultSet.getInt("ENABLE");
				Long roleID = resultSet.getLong("ROLE_ID");

				result.add(new User(id, username, password, enable, roleID));
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
	public User insertUser(User newUser) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			connection = this.getConnection();
			final String query = "INSERT INTO USERS(ID, USER_NAME, PASSWORD, ENABLE, ROLE_ID) VALUES(?, ?, ?, ?, ?)";

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			Long id = getNextIndexFromSequence();
			if(id == null)
				throw new DatabaseException("Problem by getting a sequence");
			
			newUser.setId(id);
			
			preparedStatement.setLong(i++, id);
			preparedStatement.setString(i++, newUser.getUsername());
			preparedStatement.setString(i++, newUser.getPassword());
			preparedStatement.setInt(i++, newUser.getEnable());
			preparedStatement.setLong(i++, newUser.getRoleID());
			
			System.out.println("work1");
			
			int information = preparedStatement.executeUpdate();
			
			System.out.println("work2");
			
			if(information != 0)
				System.out.println("Added successfuly!");
			else
				System.out.println("I didnt add a new record");
				
			return newUser;
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in insert", e);
		} finally {
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}
	}

	@Override
	public void deleteUser(Long id) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			if(!checkID(id))
				throw new InputException("I didnt find this user in database");
			
			connection = this.getConnection();
			final String query = "DELETE FROM USERS WHERE ID = ?";

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
	
	private boolean checkID(Long id) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM USERS WHERE ID = ?";

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
			Connector.closeConnection(connection);
		}

		return false;
	}
	
	private Long getNextIndexFromSequence() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT USERS_SEQ.NEXTVAL FROM DUAL";

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
