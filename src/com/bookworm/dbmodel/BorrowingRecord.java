package com.bookworm.dbmodel;

import java.util.Date;

/**
 * BorrowingRecord entity. @author MyEclipse Persistence Tools
 */

public class BorrowingRecord implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Book book;
	private Date borrowDate;
	private Date returnDate;

	// Constructors

	/** default constructor */
	public BorrowingRecord() {
	}

	/** full constructor */
	public BorrowingRecord(User user, Book book, Date borrowDate,
			Date returnDate) {
		this.user = user;
		this.book = book;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
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

	public Date getBorrowDate() {
		return this.borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

}