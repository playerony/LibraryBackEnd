package com.playerony.libraryV2.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.playerony.libraryV2.dao.RoleDao;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Role;
import com.playerony.libraryV2.utility.Connector;
import com.playerony.libraryV2.utility.JdbcUtility;

public class RoleDaoImpl implements RoleDao {

	public Connection getConnection() throws SQLException {
		return Connector.connect(true);
	}
	
	@Override
	public List<Role> selectRoles() throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Role> result = new ArrayList<>();

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM ROLES";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			while (resultSet.next()) {
				Long id = resultSet.getLong("ID");
				String roleName = resultSet.getString("ROLE_NAME");

				result.add(new Role(id, roleName));
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
	public Role getRoleByID(Long id) throws DatabaseException, InputException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			if(!checkID(id))
				throw new InputException("I didnt find this role in database");
			
			connection = this.getConnection();
			final String query = "SELECT * FROM ROLES WHERE ID = ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
				throw new DatabaseException("ResultSet is a null");

			if (resultSet.next()) {
				String roleName = resultSet.getString("ROLE_NAME");
				
				if(resultSet.next())
					throw new DatabaseException("I found another author with this id!");

				return new Role(id, roleName);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Database exception in getRoleByID", e);
		} finally {
			JdbcUtility.closeResultSet(resultSet);
			JdbcUtility.closeStatement(preparedStatement);
			Connector.closeConnection(connection);
		}

		return null;
	}

	private boolean checkID(Long id) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;

		try {
			connection = this.getConnection();
			final String query = "SELECT * FROM ROLES WHERE ID = ?";

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
	
}
