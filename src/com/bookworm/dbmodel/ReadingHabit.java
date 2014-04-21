package com.bookworm.dbmodel;

/**
 * ReadingHabit entity. @author MyEclipse Persistence Tools
 */

public class ReadingHabit implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private BookCategory bookCategory;

	// Constructors

	/** default constructor */
	public ReadingHabit() {
	}

	/** full constructor */
	public ReadingHabit(User user, BookCategory bookCategory) {
		this.user = user;
		this.bookCategory = bookCategory;
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

	public BookCategory getBookCategory() {
		return this.bookCategory;
	}

	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}

}