package com.bookworm.dbmodel;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ReadRecord entity. @author MyEclipse Persistence Tools
 */

public class ReadRecord implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Book book;
	private Date date;
	private Set readingNotes = new HashSet(0);

	// Constructors

	/** default constructor */
	public ReadRecord() {
	}

	/** minimal constructor */
	public ReadRecord(User user, Book book, Date date) {
		this.user = user;
		this.book = book;
		this.date = date;
	}

	/** full constructor */
	public ReadRecord(User user, Book book, Date date, Set readingNotes) {
		this.user = user;
		this.book = book;
		this.date = date;
		this.readingNotes = readingNotes;
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

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Set getReadingNotes() {
		return this.readingNotes;
	}

	public void setReadingNotes(Set readingNotes) {
		this.readingNotes = readingNotes;
	}

}