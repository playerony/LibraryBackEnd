package com.playerony.libraryV2.model;

public class User {
	private Long id;
	private String username;
	private String password;
	private Integer enable;
	private Long roleID;
	
	public User() {
		super();
	}

	public User(String username, String password, Integer enable, Long roleID) {
		super();
		this.username = username;
		this.password = password;
		this.enable = enable;
		this.roleID = roleID;
	}

	public User(Long id, String username, String password, Integer enable, Long roleID) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.enable = enable;
		this.roleID = roleID;
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

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Integer getEnable() {
		return enable;
	}

	public Long getRoleID() {
		return roleID;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public void setRoleID(Long roleID) {
		this.roleID = roleID;
	}
	
	/**
	 * 
	 * toString
	 * 
	 */
	
	public String toString() {
		return "User[id=" + this.id + " username=" + this.username + " password=" + this.password + " enable=" + this.enable + " roleID=" + this.roleID + "]";
	}
	
}
