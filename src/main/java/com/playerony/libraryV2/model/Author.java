package com.playerony.libraryV2.model;

public class Author {
	private Long id;
	private String firstName;
	private String lastName;
	private String gender;
	private Integer age;
	private String pesel;
	private Integer bookID;
	
	public Author() {
		
	}
	
	public Author(String firstName, String lastName, String gender, Integer age, String pesel) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
		this.pesel = pesel;
	}

	public Author(String firstName, String lastName, String gender, Integer age, String pesel, Integer bookID) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
		this.pesel = pesel;
		this.bookID = bookID;
	}

	public Author(Long id, String firstName, String lastName, String gender, Integer age, String pesel) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
		this.pesel = pesel;
	}

	public Author(Long id, String firstName, String lastName, String gender, Integer age, String pesel,
			Integer bookID) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
		this.pesel = pesel;
		this.bookID = bookID;
	}

	/**
	 * 
	 * Getters
	 * 
	 * @return
	 * 
	 */

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getGender() {
		return gender;
	}

	public Integer getAge() {
		return age;
	}

	public String getPesel() {
		return pesel;
	}

	public Integer getBookID() {
		return bookID;
	}
	
	/**
	 * 
	 * Setters
	 * 
	 * @return
	 * 
	 */
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}

	public void setBookID(Integer bookID) {
		this.bookID = bookID;
	}

	/**
	 * 
	 * toString
	 * 
	 */
	
	public String toString() {
		return "Author[id=" + this.id + " firstName=" + this.firstName + " lastName=" + this.lastName + " gender=" + this.gender + " age=" + this.age + " pesel=" + this.pesel + " bookID=" + bookID + "]";
	}
	
}
