package com.playerony.libraryV2.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
	private Long id;
	private String title;
	private List<Author> authors = new ArrayList<>();
	
	public Book() {
		
	}
	
	public Book(Long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	
	public Book(String title) {
		super();
		this.title = title;
	}
	
	/**
	 * 
	 * Getters
	 * 
	 * @return
	 */

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
	
	public List<Author> getAuthors(){
		return authors;
	}
	
	/**
	 * 
	 * Setters
	 * 
	 * @return
	 * 
	 */
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * toString
	 * 
	 */
	
	public String toString() {
		return "Book[id=" + id + " title=" + title + "]"; 
	}
	
}
