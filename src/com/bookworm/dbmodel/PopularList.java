package com.bookworm.dbmodel;

import java.util.Date;

/**
 * PopularList entity. @author MyEclipse Persistence Tools
 */

public class PopularList implements java.io.Serializable {

	// Fields

	private Integer id;
	private Book book;
	private Date date;

	// Constructors

	/** default constructor */
	public PopularList() {
	}

	/** full constructor */
	public PopularList(Book book, Date date) {
		this.book = book;
		this.date = date;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Book getBook() {
		return this.book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}