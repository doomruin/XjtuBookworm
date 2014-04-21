package com.bookworm.dbmodel;

import java.util.HashSet;
import java.util.Set;

/**
 * Book entity. @author MyEclipse Persistence Tools
 */

public class Book implements java.io.Serializable {

	// Fields

	private Integer id;
	private BookCategory bookCategory;
	private String name;
	private String isbn;
	private String surfacePath;
	private String author;
	private String doubanId;
	private String price;
	private String publisher;
	private String publishDate;
	private String pages;
	private String summary;
	private Set readRecords = new HashSet(0);
	private Set borrowingRecords = new HashSet(0);
	private Set popularLists = new HashSet(0);
	private Set wishs = new HashSet(0);
	private Set bookFiles = new HashSet(0);
	private Set bookComments = new HashSet(0);

	// Constructors

	/** default constructor */
	public Book() {
	}

	/** full constructor */
	public Book(BookCategory bookCategory, String name, String isbn,
			String surfacePath, String author, String doubanId, String price,
			String publisher, String publishDate, String pages, String summary,
			Set readRecords, Set borrowingRecords, Set popularLists, Set wishs,
			Set bookFiles, Set bookComments) {
		this.bookCategory = bookCategory;
		this.name = name;
		this.isbn = isbn;
		this.surfacePath = surfacePath;
		this.author = author;
		this.doubanId = doubanId;
		this.price = price;
		this.publisher = publisher;
		this.publishDate = publishDate;
		this.pages = pages;
		this.summary = summary;
		this.readRecords = readRecords;
		this.borrowingRecords = borrowingRecords;
		this.popularLists = popularLists;
		this.wishs = wishs;
		this.bookFiles = bookFiles;
		this.bookComments = bookComments;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BookCategory getBookCategory() {
		return this.bookCategory;
	}

	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsbn() {
		return this.isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getSurfacePath() {
		return this.surfacePath;
	}

	public void setSurfacePath(String surfacePath) {
		this.surfacePath = surfacePath;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDoubanId() {
		return this.doubanId;
	}

	public void setDoubanId(String doubanId) {
		this.doubanId = doubanId;
	}

	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPublisher() {
		return this.publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublishDate() {
		return this.publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getPages() {
		return this.pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Set getReadRecords() {
		return this.readRecords;
	}

	public void setReadRecords(Set readRecords) {
		this.readRecords = readRecords;
	}

	public Set getBorrowingRecords() {
		return this.borrowingRecords;
	}

	public void setBorrowingRecords(Set borrowingRecords) {
		this.borrowingRecords = borrowingRecords;
	}

	public Set getPopularLists() {
		return this.popularLists;
	}

	public void setPopularLists(Set popularLists) {
		this.popularLists = popularLists;
	}

	public Set getWishs() {
		return this.wishs;
	}

	public void setWishs(Set wishs) {
		this.wishs = wishs;
	}

	public Set getBookFiles() {
		return this.bookFiles;
	}

	public void setBookFiles(Set bookFiles) {
		this.bookFiles = bookFiles;
	}

	public Set getBookComments() {
		return this.bookComments;
	}

	public void setBookComments(Set bookComments) {
		this.bookComments = bookComments;
	}

}