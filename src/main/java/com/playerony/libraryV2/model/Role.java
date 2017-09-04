package com.playerony.libraryV2.model;

public class Role {
	private Long id;
	private String roleName;
	
	public Role() {
		super();
	}

	public Role(Long id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
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

	public String getRoleName() {
		return roleName;
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
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	/**
	 * 
	 * toString
	 * 
	 */
	
	public String toString() {
		return "Role[id=" + id + " name=" + roleName + "]"; 
	}
}
