package com.bookworm.dbmodel;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Wish entity. @author MyEclipse Persistence Tools
 */

public class Wish implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Book book;
	private String description;
	private Date postDate;
	private Set wishResponses = new HashSet(0);

	// Constructors

	/** default constructor */
	public Wish() {
	}

	/** minimal constructor */
	public Wish(User user, String description, Date postDate) {
		this.user = user;
		this.description = description;
		this.postDate = postDate;
	}

	/** full constructor */
	public Wish(User user, Book book, String description, Date postDate,
			Set wishResponses) {
		this.user = user;
		this.book = book;
		this.description = description;
		this.postDate = postDate;
		this.wishResponses = wishResponses;
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

	public Book getBook() {
		return this.book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPostDate() {
		return this.postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public Set getWishResponses() {
		return this.wishResponses;
	}

	public void setWishResponses(Set wishResponses) {
		this.wishResponses = wishResponses;
	}

}