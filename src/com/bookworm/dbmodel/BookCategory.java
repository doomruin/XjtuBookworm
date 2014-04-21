package com.bookworm.dbmodel;

import java.util.HashSet;
import java.util.Set;

/**
 * BookCategory entity. @author MyEclipse Persistence Tools
 */

public class BookCategory implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String crawlUrl;
	private Set readingHabits = new HashSet(0);
	private Set books = new HashSet(0);

	// Constructors

	/** default constructor */
	public BookCategory() {
	}

	/** full constructor */
	public BookCategory(String name, String crawlUrl, Set readingHabits,
			Set books) {
		this.name = name;
		this.crawlUrl = crawlUrl;
		this.readingHabits = readingHabits;
		this.books = books;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCrawlUrl() {
		return this.crawlUrl;
	}

	public void setCrawlUrl(String crawlUrl) {
		this.crawlUrl = crawlUrl;
	}

	public Set getReadingHabits() {
		return this.readingHabits;
	}

	public void setReadingHabits(Set readingHabits) {
		this.readingHabits = readingHabits;
	}

	public Set getBooks() {
		return this.books;
	}

	public void setBooks(Set books) {
		this.books = books;
	}

}