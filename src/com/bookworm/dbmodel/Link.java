package com.bookworm.dbmodel;

/**
 * Link entity. @author MyEclipse Persistence Tools
 */

public class Link implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private String url;
	private Integer type;

	// Constructors

	/** default constructor */
	public Link() {
	}

	/** full constructor */
	public Link(User user, String url, Integer type) {
		this.user = user;
		this.url = url;
		this.type = type;
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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}