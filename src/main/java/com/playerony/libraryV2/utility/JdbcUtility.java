package com.playerony.libraryV2.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.Statement;

import com.playerony.libraryV2.exception.DatabaseException;

public class JdbcUtility {
	public static void closeResultSet(ResultSet resultSet) throws DatabaseException {
		try {
			if (resultSet != null)
				resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeStatement(Statement statement) throws DatabaseException {
		try {
			if (statement != null)
				statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static RowId getRowId(PreparedStatement preparedStatement) throws SQLException, DatabaseException {
		ResultSet rs = preparedStatement.getGeneratedKeys();
		if (rs.next()) {
			RowId id = rs.getRowId(1);
			rs.close();
			return id;
		} else 
			throw new DatabaseException("Error ROWID");
	}
	
	public static Long getNextIndexFromSequence(Connection connection, String sequenceName) throws DatabaseException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			final String query = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";

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
		}
		
		return null;
	}
}
