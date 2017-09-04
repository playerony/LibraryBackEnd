package com.playerony.libraryV2.dao;

import java.util.List;

import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Author;

public interface CopyAuthorDao {
	Author insertCopyAuthor(Author newAuthor) throws DatabaseException, InputException;
	
	List<Author> insertCopyAuthors(List<Author> authors) throws DatabaseException, InputException;
	
	void deleteCopyAuthors() throws DatabaseException;
}
