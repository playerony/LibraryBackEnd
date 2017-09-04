package com.playerony.libraryV2.dao;

import java.util.List;

import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.User;

public interface UserDao {
	List<User> selectUsers() throws DatabaseException;
	
	User insertUser(User newUser) throws DatabaseException;
	
	void deleteUser(Long id) throws DatabaseException, InputException;
}
