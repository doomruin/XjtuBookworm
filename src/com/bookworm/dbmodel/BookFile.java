package com.bookworm.dbmodel;

/**
 * BookFile entity. @author MyEclipse Persistence Tools
 */

public class BookFile implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Book book;
	private String fileType;
	private String path;

	// Constructors

	/** default constructor */
	public BookFile() {
	}

	/** minimal constructor */
	public BookFile(User user, Book book, String path) {
		this.user = user;
		this.book = book;
		this.path = path;
	}

	/** full constructor */
	public BookFile(User user, Book book, String fileType, String path) {
		this.user = user;
		this.book = book;
		this.fileType = fileType;
		this.path = path;
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

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}