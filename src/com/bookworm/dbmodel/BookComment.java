package com.bookworm.dbmodel;

/**
 * BookComment entity. @author MyEclipse Persistence Tools
 */

public class BookComment implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Book book;
	private String comment;

	// Constructors

	/** default constructor */
	public BookComment() {
	}

	/** full constructor */
	public BookComment(User user, Book book, String comment) {
		this.user = user;
		this.book = book;
		this.comment = comment;
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

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}