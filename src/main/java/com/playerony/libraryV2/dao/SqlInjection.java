package com.playerony.libraryV2.dao;

import java.util.List;

import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.model.Author;

public interface SqlInjection {
	List<Author> selectInjection(String phrase) throws DatabaseException;
}
