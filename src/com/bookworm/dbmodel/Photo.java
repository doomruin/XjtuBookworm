package com.bookworm.dbmodel;

import java.util.HashSet;
import java.util.Set;

/**
 * Photo entity. @author MyEclipse Persistence Tools
 */

public class Photo implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private String path;
	private Set users = new HashSet(0);

	// Constructors

	/** default constructor */
	public Photo() {
	}

	/** minimal constructor */
	public Photo(User user) {
		this.user = user;
	}

	/** full constructor */
	public Photo(User user, String path, Set users) {
		this.user = user;
		this.path = path;
		this.users = users;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

}