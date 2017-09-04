package com.playerony.libraryV2.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.playerony.libraryV2.exception.DatabaseException;

public class Connector {
	private static final String URL = "jdbc:oracle:thin:@GLORA2.kamsoft.local:1521/SZKOL";
	private static final String LOGIN = "PWOJTASINSKI";
	private static final String PASSWORD = "wojtasinski";
	
	public static Connection connect(boolean autoCommit) throws SQLException{
		Connection connection = null;
		
		try{
			connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
			connection.setAutoCommit(autoCommit);
		}catch(Exception e){
			throw new SQLException("Problem by database connection", e);
		}
		
		return connection;
	}
	
	public static void closeConnection(Connection connection) throws DatabaseException{
		try{
			if(connection != null)
				connection.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
