package com.playerony.libraryV2.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.playerony.libraryV2.dao.AuthorDao;
import com.playerony.libraryV2.exception.DatabaseException;
import com.playerony.libraryV2.model.Author;

public class ListAuthorDaoImpl implements AuthorDao {
	private List<Author> authors;
	
	public ListAuthorDaoImpl() {
		authors = new ArrayList<>();
	}

	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Author insertAuthor(Author newEntity, Connection connection) throws DatabaseException {
		authors.add(newEntity);
		
		return newEntity;
	}
	
	@Override
	public List<Author> insertAuthors(List<Author> authors, Connection connection) throws DatabaseException {
		List<Author> result = new ArrayList<>();
		for(Author a : authors) {
			this.authors.add(a);
			result.add(a);
		}
		
		return result;
	}

	@Override
	public void updateAuthor(Long id, Author entity) {
		for(Author a : authors) {
			if(a.getId() == id) {
				a.setFirstName(entity.getFirstName());
				a.setLastName(entity.getLastName());
				a.setAge(entity.getAge());
				
				break;
			}
		}
	}

	@Override
	public void deleteAuthor(Long id) {
		Iterator<Author> it = authors.iterator();
		
		while(it.hasNext()) {
			if(it.next().getId() == id) {
				it.remove();
				break;
			}
		}
	}

	@Override
	public List<Author> selectAuthors() {
		return authors;
	}

	@Override
	public Author getAuthorById(Long id) {
		for(Author a : authors)
			if(a.getId() == id)
				return a;
		
		return null;
	}

	@Override
	public List<Author> getAuthorByAge(Integer age) {
		List<Author> authors = new ArrayList<>();
		
		for(Author a : authors)
			if(a.getAge() == age)
				authors.add(a);
		
		return authors;
	}

	@Override
	public List<Author> getAuthorByFirstName(String firstName) {
		List<Author> authors = new ArrayList<>();
		
		for(Author a : authors)
			if(a.getFirstName().equals(firstName))
				authors.add(a);
		
		return authors;
	}

	@Override
	public List<Author> getAuthorByLastName(String lastName) {
		List<Author> authors = new ArrayList<>();
		
		for(Author a : authors)
			if(a.getFirstName().equals(lastName))
				authors.add(a);
		
		return authors;
	}

	@Override
	public List<Long> getUsersByBookID(Long id) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet getAuthorsResultSet() {
		// TODO Auto-generated method stub
		return null;
	}

}
