package com.playerony.libraryV2.dao;

import java.util.List;

import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.exception.InputException;
import com.playerony.libraryV2.model.Book;

public interface CopyBookDao {
	Book insertCopyBook(Book newBook) throws DatabaseException, InputException;
	
	List<Book> insertCopyBooks(List<Book> books) throws DatabaseException, InputException;
	
	void deleteCopyBooks() throws DatabaseException;
}
