package com.playerony.libraryV2.dao;

import com.playerony.libraryV2.exception.DatabaseException;

public interface Copy {
	void copyAuthorTable() throws DatabaseException;
}
